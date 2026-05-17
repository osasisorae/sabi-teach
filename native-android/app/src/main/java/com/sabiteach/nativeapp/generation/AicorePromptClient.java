package com.sabiteach.nativeapp.generation;

import androidx.annotation.NonNull;

import com.google.mlkit.genai.common.DownloadCallback;
import com.google.mlkit.genai.common.FeatureStatus;
import com.google.mlkit.genai.common.GenAiException;
import com.google.mlkit.genai.prompt.GenerateContentRequest;
import com.google.mlkit.genai.prompt.GenerateContentResponse;
import com.google.mlkit.genai.prompt.Generation;
import com.google.mlkit.genai.prompt.TextPart;
import com.google.mlkit.genai.prompt.java.GenerativeModelFutures;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

final class AicorePromptClient {
    static final int STATUS_UNAVAILABLE = FeatureStatus.UNAVAILABLE;
    static final int STATUS_DOWNLOADABLE = FeatureStatus.DOWNLOADABLE;
    static final int STATUS_DOWNLOADING = FeatureStatus.DOWNLOADING;
    static final int STATUS_AVAILABLE = FeatureStatus.AVAILABLE;

    private final GenerativeModelFutures generativeModelFutures =
        GenerativeModelFutures.from(Generation.INSTANCE.getClient());

    private boolean warmedUp = false;

    int checkStatus() throws ExecutionException, InterruptedException {
        return generativeModelFutures.checkStatus().get();
    }

    void ensureReady() throws ExecutionException, InterruptedException {
        int status = checkStatus();

        if (status == STATUS_DOWNLOADABLE) {
            AtomicReference<GenAiException> downloadFailure = new AtomicReference<>();
            generativeModelFutures.download(new DownloadCallback() {
                @Override
                public void onDownloadStarted(long bytesToDownload) {
                }

                @Override
                public void onDownloadProgress(long totalBytesDownloaded) {
                }

                @Override
                public void onDownloadCompleted() {
                }

                @Override
                public void onDownloadFailed(@NonNull GenAiException e) {
                    downloadFailure.set(e);
                }
            }).get();

            if (downloadFailure.get() != null) {
                throw new IllegalStateException(
                    "Gemini Nano download failed: " + downloadFailure.get().getMessage(),
                    downloadFailure.get()
                );
            }

            status = checkStatus();
        }

        if (status != STATUS_AVAILABLE) {
            throw new IllegalStateException("On-device model is not ready. Status=" + status);
        }

        if (!warmedUp) {
            generativeModelFutures.warmup().get();
            warmedUp = true;
        }
    }

    String generateText(String prompt, int maxOutputTokens)
        throws ExecutionException, InterruptedException {
        GenerateContentRequest.Builder builder = new GenerateContentRequest.Builder(new TextPart(prompt));
        builder.setTemperature(0.2f);
        builder.setTopK(20);
        builder.setCandidateCount(1);
        builder.setMaxOutputTokens(maxOutputTokens);

        GenerateContentResponse response = generativeModelFutures.generateContent(builder.build()).get();
        if (response.getCandidates().isEmpty()) {
            throw new IllegalStateException("On-device generator returned no candidates.");
        }

        String text = response.getCandidates().get(0).getText();
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalStateException("On-device generator returned an empty response.");
        }

        return text;
    }
}
