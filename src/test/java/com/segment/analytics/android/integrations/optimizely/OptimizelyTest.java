package com.segment.analytics.android.integrations.optimizely;

import android.app.Application;
import com.optimizely.Optimizely;
import com.optimizely.integration.OptimizelyExperimentData;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.core.tests.BuildConfig;
import com.segment.analytics.integrations.Logger;
import com.segment.analytics.test.IdentifyPayloadBuilder;
import com.segment.analytics.test.TrackPayloadBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.segment.analytics.Analytics.LogLevel.VERBOSE;
import static com.segment.analytics.Utils.createTraits;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18, manifest = Config.NONE)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({ Optimizely.class }) //
public class OptimizelyTest {

  @Rule public PowerMockRule rule = new PowerMockRule();
  @Mock Application application;
  @Mock Analytics analytics;

  OptimizelyIntegration integration;

  @Before public void setUp() {
    initMocks(this);
    PowerMockito.mockStatic(Optimizely.class);

    integration = new OptimizelyIntegration(analytics, application, Logger.with(VERBOSE));
  }

  @Test public void identify() {
    integration.identify(new IdentifyPayloadBuilder().traits(createTraits("foo")).build());

    verifyStatic();
    Optimizely.setUserId(application, "foo");
  }

  @Test public void track() {
    integration.track(new TrackPayloadBuilder().event("foo").build());

    verifyStatic();
    Optimizely.trackEvent("foo");
  }

  @Test public void trackWithRevenue() {
    TrackPayloadBuilder builder = new TrackPayloadBuilder() //
        .event("qaz") //
        .properties(new Properties().putRevenue(10));
    integration.track(builder.build());

    verifyStatic();
    Optimizely.trackRevenueWithDescription(10, "qaz");
  }

  @Test public void root() {
    OptimizelyExperimentData data = new OptimizelyExperimentData();
    data.experimentId = "bar";
    data.experimentName = "foo";
    data.variationId = "qux";
    data.variationName = "qaz";

    integration.onOptimizelyExperimentViewed(data);

    verify(analytics).track("Experiment Viewed", new Properties() //
        .putValue("experimentId", "bar")
        .putValue("experimentName", "foo")
        .putValue("variationId", "qux")
        .putValue("variationName", "qaz"));
  }
}
