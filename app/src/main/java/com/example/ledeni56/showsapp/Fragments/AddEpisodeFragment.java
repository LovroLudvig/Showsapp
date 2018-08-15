package com.example.ledeni56.showsapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ledeni56.showsapp.Activities.BasicActivity;
import com.example.ledeni56.showsapp.Activities.MainActivity;
import com.example.ledeni56.showsapp.Interfaces.ApiService;
import com.example.ledeni56.showsapp.Networking.ApiEpisode;
import com.example.ledeni56.showsapp.Networking.ApiServiceFactory;
import com.example.ledeni56.showsapp.Networking.EpisodeUpload;
import com.example.ledeni56.showsapp.Networking.MediaResponse;
import com.example.ledeni56.showsapp.Networking.ResponseData;
import com.example.ledeni56.showsapp.Room.DatabaseCallback;
import com.example.ledeni56.showsapp.Room.ShowsAppRepository;
import com.example.ledeni56.showsapp.Static.ApplicationShows;
import com.example.ledeni56.showsapp.Entities.Episode;
import com.example.ledeni56.showsapp.Entities.Show;
import com.example.ledeni56.showsapp.R;
import com.example.ledeni56.showsapp.Interfaces.ToolbarProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;


public class AddEpisodeFragment extends Fragment {
    private static final String ARG_NUMBER = "ARG_NUMBER";
    private static final int REQUEST_CODE_PERMISSION_GALLERY = 1;
    private static final int REQUEST_CODE_PICTURE_FROM_GALLERY = 2;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;

    private String showId;
    private Show show;
    private Uri episodeUriPicture = null;

    private EditText episodeNameView;
    private EditText episodeDescriptionView;
    private TextView selectedEpisodeText;
    private Button saveButton;
    private RelativeLayout addPhotoLayout;
    private ImageView episodePhoto;
    private ImageView addPhotoIcon;
    private TextView addPhotoText;
    private View episodePicker;

    private Episode addedEpisode;
    private int episodeNumber;
    private int seasonNumber;
    private ToolbarProvider listener;
    private Toolbar toolbar;
    private BasicActivity activity;
    private ShowsAppRepository showsAppRepository;
    private FragmentManager fragmentManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_episode, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        show = ApplicationShows.getShow(showId);

        episodeNameView = view.findViewById(R.id.episodeName);
        episodeDescriptionView = view.findViewById(R.id.episodeDescription);
        saveButton = view.findViewById(R.id.saveButton);
        selectedEpisodeText = view.findViewById(R.id.selectedEpisode);
        addPhotoLayout = view.findViewById(R.id.addPhotoLayout);
        episodePhoto = view.findViewById(R.id.episodeImage);
        addPhotoText = view.findViewById(R.id.addPhotoText);
        addPhotoIcon = view.findViewById(R.id.addPhotoIcon);
        episodePicker = view.findViewById(R.id.episodePicker);

        fragmentManager = getFragmentManager();
        if (showsAppRepository == null) {
            showsAppRepository = ShowsAppRepository.get(getActivity());
        }

        toolbar = listener.getToolbar();
        setMyToolbar();
        initAddPhotoView();
        saveButtonInit();
        episodePickerViewInit();

    }

    private void episodePickerViewInit() {
        episodePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.episode_picker);

                Button saveButton = dialog.findViewById(R.id.dialogSave);
                final NumberPicker seasonPicker = dialog.findViewById(R.id.dialogSeason);
                final NumberPicker episodePicker = dialog.findViewById(R.id.dialogEpisode);

                seasonPicker.setMinValue(1);
                seasonPicker.setMaxValue(20);

                episodePicker.setMaxValue(99);
                episodePicker.setMinValue(1);

                episodePicker.setWrapSelectorWheel(false);
                seasonPicker.setWrapSelectorWheel(false);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        episodeNumber = episodePicker.getValue();
                        seasonNumber = seasonPicker.getValue();
                        selectedEpisodeText.setText("Season " + String.valueOf(seasonNumber) + ", Ep " + String.valueOf(episodeNumber));
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    private void saveButtonInit() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.isInternetAvailable()) {
                    if (checkFields()) {
                        if (addEpisode()) {
                            uploadMedia();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Internet is not available.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initAddPhotoView() {
        addPhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.camera_gallery_picker);
                Button galleryButton = dialog.findViewById(R.id.galleryButton);
                Button cameraButton = dialog.findViewById(R.id.cameraButton);

                cameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_CAMERA);
                        } else {
                            loadFromCamera();
                        }
                    }
                });

                galleryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_GALLERY);
                        } else {
                            loadFromGallery();
                        }
                    }
                });
                dialog.show();

            }
        });
    }

    private void uploadMedia() {
        File file;
        file = new File(episodeUriPicture.getPath());
        activity.showProgress();
        ApiServiceFactory.get().uploadMedia(((MainActivity) getActivity()).getUserToken(), RequestBody.create(MediaType.parse("image/jpg"), file)).enqueue(new Callback<ResponseData<MediaResponse>>() {
            @Override
            public void onResponse(Call<ResponseData<MediaResponse>> call, Response<ResponseData<MediaResponse>> response) {
                if (response.isSuccessful()) {
                    uploadEpisode(response);
                } else {
                    activity.hideProgress();
                    activity.showError("Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<MediaResponse>> call, Throwable t) {
                activity.hideProgress();
                activity.showError("Error");
            }
        });
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void uploadEpisode(Response<ResponseData<MediaResponse>> response) {
        ApiServiceFactory.get().uploadEpisode(((MainActivity) getActivity()).getUserToken(),
                new EpisodeUpload(showId, response.body().getData().getMediaId(), addedEpisode.getName(), addedEpisode.getDescription(), Integer.toString(addedEpisode.getEpisodeNumber()), Integer.toString(addedEpisode.getSeasonNumber()))).enqueue(new Callback<ResponseData<ApiEpisode>>() {
            @Override
            public void onResponse(Call<ResponseData<ApiEpisode>> call, Response<ResponseData<ApiEpisode>> response) {
                if (response.isSuccessful()) {
                    saveEpisode(response);
                } else {
                    activity.hideProgress();
                    activity.showError("Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseData<ApiEpisode>> call, Throwable t) {
                activity.hideProgress();
                activity.showError("Error");
            }
        });
    }

    private void saveEpisode(Response<ResponseData<ApiEpisode>> response) {
        ApiEpisode episode = response.body().getData();
        ApplicationShows.addEpisodeToShow(new Episode(episode.getId(), showId, episode.getName(), episode.getDescription(), episode.getEpisodeNumber(), episode.getSeasonNumber(), episode.getPicture()), showId);
        activity.hideKeyboard();
        activity.hideProgress();
        Collections.sort(ApplicationShows.getShow(showId).getEpisodes());
        showsAppRepository.insertEpisodes(ApplicationShows.getShow(showId).getEpisodes(), new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
                EpisodeSelectFragment episodeSelectFragment = EpisodeSelectFragment.newInstance(showId);
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit).replace(R.id.frameLayoutRight, episodeSelectFragment).addToBackStack("episode").commit();
            }

            @Override
            public void onError(Throwable t) {
                activity.showError("Unknown Error");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        toolbar.setVisibility(GONE);
    }

    private void setMyToolbar() {
        toolbar = listener.getToolbar();
        toolbar.getMenu().clear();
        toolbar.setTitle("Add episode");
        toolbar.inflateMenu(R.menu.menu_episode);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.getMenu().findItem(R.id.action_add).setVisible(false);
    }

    private boolean addEpisode() {
        addedEpisode = new Episode(
                UUID.randomUUID().toString(),
                showId,
                episodeNameView.getText().toString(),
                episodeDescriptionView.getText().toString(),
                episodeNumber,
                seasonNumber,
                episodeUriPicture.toString()
        );

        if (!ApplicationShows.getShow(showId).getEpisodes().contains(addedEpisode)) {
            return true;
        } else {
            Toast.makeText(getContext(), "Episode already exists, please check Season and Episode", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkFields() {
        if (episodeNameView.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedEpisodeText.getText().toString().equals("Unknown")) {
            Toast.makeText(getContext(), "Select Episode and Season is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (episodeDescriptionView.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Description is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (episodeDescriptionView.getText().toString().length()<50) {
            Toast.makeText(getContext(), "Description length should be at least 50 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ApplicationShows.nameExists(episodeNameView.getText().toString(), showId)) {
            Toast.makeText(getContext(), "Name already exists, please check Title", Toast.LENGTH_SHORT).show();
            return false;
        } else if (episodeUriPicture == null) {
            Toast.makeText(getContext(), "Please select a picture", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CODE_PERMISSION_GALLERY) {
                loadFromGallery();
            } else if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
                loadFromCamera();
            }
        } else {
            Toast.makeText(getContext(), "Need permission to do that action", Toast.LENGTH_SHORT);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        episodeUriPicture = Uri.fromFile(image);
        return image;
    }

    private void loadFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK);

        String picturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        Uri data = Uri.parse(picturesPath);

        i.setDataAndType(data, "image/*");
        startActivityForResult(i, REQUEST_CODE_PICTURE_FROM_GALLERY);
    }

    private void loadFromCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.example.android.fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICTURE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri result = data.getData();
            Glide.with(this).load(result).into(episodePhoto);
            addPhotoIcon.setVisibility(GONE);
            addPhotoText.setVisibility(GONE);
            episodeUriPicture = Uri.fromFile(new File(getRealPathFromUri(getActivity(), result)));

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Glide.with(this).load(episodeUriPicture).into(episodePhoto);
            addPhotoIcon.setVisibility(GONE);
            addPhotoText.setVisibility(GONE);
        }
    }

    public static AddEpisodeFragment newInstance(String showId) {
        AddEpisodeFragment fragment = new AddEpisodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER, showId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showId = getArguments().getString(ARG_NUMBER);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarProvider) {
            listener = (ToolbarProvider) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToolbarProvider");
        }
        activity = (BasicActivity) context;
    }

    public Bundle getCurrentFields() {
        Bundle bun = new Bundle();
        bun.putString("episode name", episodeNameView.getText().toString());
        bun.putString("episode desc", episodeDescriptionView.getText().toString());
        bun.putString("episode and season text", selectedEpisodeText.getText().toString());
        bun.putParcelable("uri", episodeUriPicture);
        return bun;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
