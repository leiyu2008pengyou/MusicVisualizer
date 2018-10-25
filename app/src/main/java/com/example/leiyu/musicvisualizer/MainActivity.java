package com.example.leiyu.musicvisualizer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private VisualizerView mVisualizerView;
  private MediaPlayer mMediaPlayer;
  private Visualizer mVisualizer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mVisualizerView = findViewById(R.id.visualizerview);
    mMediaPlayer = MediaPlayer.create(this, R.raw.videodemo);

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
          1);
    } else {
      init();
    }
  }

  private void init() {
    mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
    mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
    mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
      @Override
      public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

      }

      @Override
      public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        mVisualizerView.updateVisualizer(fft);
      }
    }, Visualizer.getMaxCaptureRate() / 2, true, true);
    mVisualizer.setEnabled(true);
    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        mVisualizer.setEnabled(false);
      }
    });
    mMediaPlayer.start();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == 1) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        init();
      } else {
        // Permission Denied
        Toast.makeText(MainActivity.this, "请打开录音权限", Toast.LENGTH_SHORT).show();
      }
      return;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mMediaPlayer != null) {
      mMediaPlayer.release();
      mMediaPlayer = null;
    }
    if (mVisualizer != null) {
      mVisualizer.release();
    }
  }
}
