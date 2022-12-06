//package com.ifasport.application.child;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//import com.coremedia.iso.boxes.Container;
//import com.googlecode.mp4parser.authoring.Movie;
//import com.googlecode.mp4parser.authoring.Track;
//import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
//import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
//import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
//import com.ifasport.application.R;
//
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.channels.FileChannel;
//import java.util.LinkedList;
//import java.util.List;
//
//public class Mp4Parser extends AppCompatActivity { //ActionBarActivity
//	private final String TAG = this.getClass().getSimpleName();
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.child_activity_mp_parser);
//
//
//
//
//	}
//
//	public String appendVideo(String[] videos) throws IOException{
//		Log.v(TAG, "in appendVideo() videos length is " + videos.length);
//		Movie[] inMovies = new Movie[videos.length];
//		int index = 0;
//		for(String video: videos)
//		{
//			Log.i(TAG, "    in appendVideo one video path = " + video);
//			inMovies[index] = MovieCreator.build(video);
//			index++;
//		}
//		List<Track> videoTracks = new LinkedList<Track>();
//		List<Track> audioTracks = new LinkedList<Track>();
//		for (Movie m : inMovies) {
//			for (Track t : m.getTracks()) {
//				if (t.getHandler().equals("soun")) {
//					audioTracks.add(t);
//				}
//				if (t.getHandler().equals("vide")) {
//					videoTracks.add(t);
//				}
//			}
//		}
//
//		Movie result = new Movie();
//		Log.v(TAG, "audioTracks size = " + audioTracks.size()
//				+ " videoTracks size = " + videoTracks.size());
//		if (audioTracks.size() > 0) {
//			result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
//		}
//		if (videoTracks.size() > 0) {
//			result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
//		}
//		String videoCombinePath = RecordUtil.createFinalPath(Mp4Parser.this);
//		Container out = new DefaultMp4Builder().build(result);
//		FileChannel fc = new RandomAccessFile(videoCombinePath, "rw").getChannel();
//		out.writeContainer(fc);
//		fc.close();
//		Log.v(TAG, "after combine videoCombinepath = " + videoCombinePath);
//		return videoCombinePath;
//	}
//}
