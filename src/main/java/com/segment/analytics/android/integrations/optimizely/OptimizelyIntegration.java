package com.segment.analytics.android.integrations.optimizely;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.optimizely.Optimizely;
import com.optimizely.integration.OptimizelyEventListener;
import com.optimizely.integration.OptimizelyExperimentData;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.ValueMap;
import com.segment.analytics.integrations.IdentifyPayload;
import com.segment.analytics.integrations.Integration;
import com.segment.analytics.integrations.Logger;
import com.segment.analytics.integrations.TrackPayload;
import java.util.List;

/**
 * Kahuna helps mobile marketers send push notifications and in-app messages.
 *
 * @see <a href="https://www.optimizely.com/">Pptimizely</a>
 * @see <a href="https://segment.com/docs/integrations/optimizely/">Optimizely Integration</a>
 * @see <a href="http://developers.optimizely.com/android/">Optimizely Android SDK</a>
 */
public class OptimizelyIntegration extends Integration<Void> implements OptimizelyEventListener {
  private static final String OPTIMIZELY_KEY = "Optimizely";

  private final Context context;
  private final Logger logger;
  private final Analytics analytics;

  // Optimizely must be initialized immediately on launch. So we require the token when creating the
  // factory, and initialize it in this method.
  public static Factory factory(final Application application, String token) {
    Optimizely.startOptimizelyWithAPIToken(token, application);
    return new Factory() {
      @Override public Integration<?> create(ValueMap settings, Analytics analytics) {
        boolean listen = settings.getBoolean("listen", false);
        Logger logger = analytics.logger(OPTIMIZELY_KEY);

        OptimizelyIntegration integration =
            new OptimizelyIntegration(analytics, application, logger);
        if (listen) {
          Optimizely.addOptimizelyEventListener(integration);
        }
        return integration;
      }

      @Override public String key() {
        return OPTIMIZELY_KEY;
      }
    };
  }

  public OptimizelyIntegration(Analytics analytics, Context context, Logger logger) {
    this.analytics = analytics;
    this.context = context;
    this.logger = logger;
  }

  @Override public void identify(IdentifyPayload identify) {
    super.identify(identify);

    String userId = identify.userId();
    Optimizely.setUserId(context, userId);
    logger.verbose("Optimizely.setUserId(context, %s);", userId);
  }

  @Override public void track(TrackPayload track) {
    super.track(track);

    String event = track.event();
    int revenue = (int) track.properties().revenue();
    if (revenue == 0) {
      Optimizely.trackEvent(event);
      logger.verbose("Optimizely.trackEvent(%s);", event);
    } else {
      Optimizely.trackRevenueWithDescription(revenue, track.event());
      logger.verbose("Optimizely.trackEvent(%d, %s);", revenue, event);
    }
  }

  @Override public void onOptimizelyStarted() {
    // Ignore.
  }

  @Override public void onOptimizelyFailedToStart(String s) {
    // Ignore.
  }

  @Override
  public void onOptimizelyExperimentVisited(OptimizelyExperimentData optimizelyExperimentData) {
    // Ignore.
  }

  @Override
  public void onOptimizelyExperimentViewed(OptimizelyExperimentData optimizelyExperimentData) {
    analytics.track("Experiment Viewed", new Properties() //
        .putValue("experimentId", optimizelyExperimentData.experimentId)
        .putValue("experimentName", optimizelyExperimentData.experimentName)
        .putValue("variationId", optimizelyExperimentData.variationId)
        .putValue("variationName", optimizelyExperimentData.variationName));
  }

  @Override public void onOptimizelyEditorEnabled() {
    // Ignore.
  }

  @Override public void onOptimizelyDataFileLoaded() {
    // Ignore.
  }

  @Override public void onGoalTriggered(String s, List<OptimizelyExperimentData> list) {
    // Ignore.
  }

  @Override public void onMessage(String s, String s1, Bundle bundle) {
    // Ignore.
  }
}
