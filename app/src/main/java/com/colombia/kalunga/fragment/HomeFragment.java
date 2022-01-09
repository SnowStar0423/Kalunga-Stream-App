package com.colombia.kalunga.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.colombia.kalunga.R;
import com.colombia.kalunga.service.StreamService;

public class HomeFragment extends Fragment {
    private SeekBar volumeSeekbar;
    private ImageView facebook_btn, instagram_btn, whatsapp_btn, play_btn, stop_btn, mute_btn, share_btn;
    boolean play_status = true, mute_status = false;
    int volume, currentVolume;
    private static final String CHANNEL_ID = "notification_channel_01";
    private static final String NOTIFICATION_Service_CHANNEL_ID = "service_channel";
    private AudioManager audioManager = null;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        root.setFocusableInTouchMode(true);
        root.setFocusable(true);
        root.requestFocus();


        facebook_btn = (ImageView) root.findViewById(R.id.facebook_btn);
        instagram_btn = (ImageView) root.findViewById(R.id.instagram_btn);
        whatsapp_btn = (ImageView) root.findViewById(R.id.whatsapp_btn);
        play_btn = (ImageView) root.findViewById(R.id.play_btn);
        mute_btn = (ImageView) root.findViewById(R.id.mute_btn);
        share_btn = (ImageView) root.findViewById(R.id.share_btn);
        volumeSeekbar = (SeekBar) root.findViewById(R.id.seekBar);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekbar.setProgress(currentVolume);
        if (currentVolume == 0) {
            mute_status = true;
            setMute(mute_status);
        }

//        Intent intent = new Intent(getActivity(), StreamService.class);
//        intent.putExtra("action", "play");
//        getActivity().startService(intent);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Sync Service", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Service Name");
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon);
        Intent notificationIntent1 = new Intent(getActivity(), getActivity().getClass());
        notificationIntent1.setAction("PLAY_ACTION");
        notificationIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent1.putExtra("action", "play");
        PendingIntent pendingIntent1 = PendingIntent.getActivity(getActivity(), 0, notificationIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notificationIntent2 = new Intent(getActivity(), getActivity().getClass());
        notificationIntent2.setAction("STOP_ACTION");
        notificationIntent2.putExtra("action", "stop");
        PendingIntent pendingIntent2 = PendingIntent.getActivity(getActivity(), 0, notificationIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
        // Build the notification and add the action.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID )
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(largeIcon)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Ths is a notification text")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Ths is a notification text of Kalunga Radio"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent1)
                .addAction(R.drawable.stop_icon, getString(R.string.stop), pendingIntent2);
//                .setAutoCancel(true);
        // Issue the new notification.
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());

        Intent i = new Intent(getActivity(), StreamService.class);
        String action = getActivity().getIntent().getStringExtra("action");
        if (action != null && action.equals("play")) {
            play_status = false;
            i.putExtra("action", "play");
            getActivity().startService(i);
            play_btn.setImageResource(R.drawable.stop);
        } else if (action != null && action.equals("stop")) {
            play_status = true;
            getActivity().stopService(i);
            notificationManagerCompat.cancel(100);
            play_btn.setImageResource(R.drawable.play);
        }

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (play_status) {
                    notificationManagerCompat.notify(100, builder.build());
                } else {
                    getActivity().stopService(i);
                    notificationManagerCompat.cancel(100);
                    play_btn.setImageResource(R.drawable.play);
                }

            }
        });


        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_VOLUME_UP ) {
                    volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1);
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
                    setMute(true);
                    return true;
                }
                return false;
            }
        });

        facebook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        instagram_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://instagram.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        whatsapp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.whatsapp.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

//        play_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                play_status = !play_status;
//                mediaPlay(play_status);
//            }
//        });

        mute_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mute_status = !mute_status;
                setMute(mute_status);
            }
        });

        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                volume = progress;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
                if (volume == 0) {
                    setMute(true);
                } else {
                    setMute(false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });



        return root;
    }

    private void mediaPlay(boolean status) {
        if (status) {
            play_btn.setImageResource(R.drawable.pause);
            Intent intent = new Intent(getActivity(), StreamService.class);
            intent.putExtra("action", "resume");
            getActivity().startService(intent);
        } else {
            play_btn.setImageResource(R.drawable.play);
            Intent intent = new Intent(getActivity(), StreamService.class);
            intent.putExtra("action", "pause");
            getActivity().startService(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setMute(boolean status) {
        if (status) {
            mute_btn.setImageResource(R.drawable.mute);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 2);
        } else {
            mute_btn.setImageResource(R.drawable.speaker);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 3);
        }
    }

}