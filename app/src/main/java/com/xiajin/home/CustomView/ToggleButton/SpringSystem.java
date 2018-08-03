package com.xiajin.home.CustomView.ToggleButton;

/**
 * This is a wrapper for BaseSpringSystem that provides the convenience of automatically providing
 * the AndroidSpringLooper dependency in {@link com.xiajin.home.CustomView.ToggleButton.SpringSystem#create}.
 */
public class SpringSystem extends BaseSpringSystem {

  /**
   * Create a new SpringSystem providing the appropriate constructor parameters to work properly
   * in an Android environment.
   * @return the SpringSystem
   */
  public static SpringSystem create() {
    return new SpringSystem(AndroidSpringLooperFactory.createSpringLooper());
  }

  private SpringSystem(SpringLooper springLooper) {
    super(springLooper);
  }

}