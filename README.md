analytics-android-integration-optimizely
========================================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.segment.analytics.android.integrations/optimizely/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.segment.analytics.android.integrations/optimizely)
[![Javadocs](http://javadoc-badge.appspot.com/com.segment.analytics.android.integrations/optimizely.svg?label=javadoc)](http://javadoc-badge.appspot.com/com.segment.analytics.android.integrations/optimizely)

Optimizely integration for [analytics-android](https://github.com/segmentio/analytics-android).

## Installation

To install the Segment-Optimizely integration, simply add this line to your gradle file:

```
compile 'com.segment.analytics.android.integrations:optimizely:+'
```

## Usage

Since Optimizely needs to be initialized as early as possible during your application lifecycle, this integration is not initialized directly by Segment after fetching settings. Instead, we initialize Optimizely when you create the factory.

```
Analytics analytics = new Analytics.Builder(context, writeKey)
  .use(OptimizelyIntegration.factory(application, optimizelyToken))
  ...
  .build();
```

Note the different syntax used here - instead of using a global singleton instance, we use a method that creates the factory on demand.

Please see [our documentation](https://segment.com/docs/integrations/optimizely/) for more information.

## License

```
WWWWWW||WWWWWW
 W W W||W W W
      ||
    ( OO )__________
     /  |           \
    /o o|    MIT     \
    \___/||_||__||_|| *
         || ||  || ||
        _||_|| _||_||
       (__|__|(__|__|

The MIT License (MIT)

Copyright (c) 2014 Segment, Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
