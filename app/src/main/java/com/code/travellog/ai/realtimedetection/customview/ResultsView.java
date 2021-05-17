

package com.code.travellog.ai.realtimedetection.customview;

import com.code.travellog.ai.realtimedetection.aiboost.Classifier;



import java.util.List;

public interface ResultsView {
  public void setResults(final List<Classifier.Recognition> results);
}
