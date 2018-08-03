package com.xiajin.home.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiajin.home.R;
import com.xiajin.home.CustomView.SwipeBack.SwipeBackActivity;
import com.xiajin.home.CustomView.adoutOus.ShimmerFrameLayout;
import com.xiajin.home.base.BaseActivity;

public class AboutOus extends SwipeBackActivity {
	
	  private ShimmerFrameLayout mShimmerViewContainer;
	  private Button[] mPresetButtons;
	  private int mCurrentPreset = -1;
	  private Toast mPresetToast;
	
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.activity_about_ous);
	initTopBarForLeft("about Ous");
	mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

    mPresetButtons = new Button[]{
        (Button) findViewById(R.id.preset_button0),
        (Button) findViewById(R.id.preset_button1),
        (Button) findViewById(R.id.preset_button2),
        (Button) findViewById(R.id.preset_button3),
        (Button) findViewById(R.id.preset_button4),
    };
    for (int i = 0; i < mPresetButtons.length; i++) {
      final int preset = i;
      mPresetButtons[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          selectPreset(preset, true);
        }
      });
    }
  }

  @Override
  protected void onStart() {
    super.onStart();

    selectPreset(0, false);
  }

  @Override
  public void onResume() {
    super.onResume();
    mShimmerViewContainer.startShimmerAnimation();
  }

  @Override
  public void onPause() {
    mShimmerViewContainer.stopShimmerAnimation();
    super.onPause();
  }

  /**
   * Select one of the shimmer animation presets.
   *
   * @param preset index of the shimmer animation preset
   * @param showToast whether to show a toast describing the preset, or not
   */
  private void selectPreset(int preset, boolean showToast) {
    if (mCurrentPreset == preset) {
      return;
    }
    if (mCurrentPreset >= 0) {
      mPresetButtons[mCurrentPreset].setBackgroundResource(R.color.preset_button_background);
    }
    mCurrentPreset = preset;
    mPresetButtons[mCurrentPreset].setBackgroundResource(R.color.preset_button_background_selected);

    // Save the state of the animation
    boolean isPlaying = mShimmerViewContainer.isAnimationStarted();

    // Reset all parameters of the shimmer animation
    mShimmerViewContainer.useDefaults();

    // If a toast is already showing, hide it
    if (mPresetToast != null) {
      mPresetToast.cancel();
    }

    switch (preset) {
      default:
      case 0:
        // Default
        mPresetToast = Toast.makeText(this, "Default", Toast.LENGTH_SHORT);
        break;
      case 1:
        // Slow and reverse
        mShimmerViewContainer.setDuration(5000);
        mShimmerViewContainer.setRepeatMode(ObjectAnimator.REVERSE);
        mPresetToast = Toast.makeText(this, "Slow and reverse", Toast.LENGTH_SHORT);
        break;
      case 2:
        // Thin, straight and transparent
        mShimmerViewContainer.setBaseAlpha(0.1f);
        mShimmerViewContainer.setDropoff(0.1f);
        mShimmerViewContainer.setTilt(0);
        mPresetToast = Toast.makeText(this, "Thin, straight and transparent", Toast.LENGTH_SHORT);
        break;
      case 3:
        // Sweep angle 90
        mShimmerViewContainer.setAngle(ShimmerFrameLayout.MaskAngle.CW_90);
        mPresetToast = Toast.makeText(this, "Sweep angle 90", Toast.LENGTH_SHORT);
        break;
      case 4:
        // Spotlight
        mShimmerViewContainer.setBaseAlpha(0);
        mShimmerViewContainer.setDuration(2000);
        mShimmerViewContainer.setDropoff(0.1f);
        mShimmerViewContainer.setIntensity(0.35f);
        mShimmerViewContainer.setMaskShape(ShimmerFrameLayout.MaskShape.RADIAL);
        mPresetToast = Toast.makeText(this, "Spotlight", Toast.LENGTH_SHORT);
        break;
    }

    // Show toast describing the chosen preset, if necessary
    if (showToast) {
      mPresetToast.show();
    }

    // Setting a value on the shimmer layout stops the animation. Restart it, if necessary.
    if (isPlaying) {
      mShimmerViewContainer.startShimmerAnimation();
    }
  }
  @Override
  public void retry() {

  }

  @Override
  public void netError() {

  }
}
