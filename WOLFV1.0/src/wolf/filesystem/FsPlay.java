package wolf.filesystem;

import wolf.project.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.VideoView;

public class FsPlay extends Activity {
	private static String videoPath;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.and_video_play);

		Intent Inte_videoPath = getIntent();
		videoPath = Inte_videoPath.getStringExtra("Vfile_Path");

		VideoView video = (VideoView) findViewById(R.id.videoView);
		video.setVideoPath(videoPath);
		video.start();

	}
}
