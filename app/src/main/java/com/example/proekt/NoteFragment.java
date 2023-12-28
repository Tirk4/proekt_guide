package com.example.proekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NoteFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker currentMarker;

    private static View rootView;
    public static NoteFragment instance;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        instance=this;

        rootView = inflater.inflate(R.layout.fragment_note, container, false);

        Bundle args = getArguments();
        Note note = (Note) args.getSerializable("note");

        ((EditText) rootView.findViewById(R.id.editTextTitle)).setText(note.title);
        ((EditText) rootView.findViewById(R.id.editTextTopic)).setText(note.theme);
        ((EditText) rootView.findViewById(R.id.editTextNote)).setText(note.text);
        ((RatingBar) rootView.findViewById(R.id.ratingBar)).setRating(note.rate);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        Note note = (Note) args.getSerializable("note");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView1);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        ImageButton trashButton = rootView.findViewById(R.id.trashButton);
        trashButton.setOnClickListener(v -> {
            DataBase.getInstance().delete(note.getId());

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        ImageButton saveButton = rootView.findViewById(R.id.saveReadButton);
        saveButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);


            note.title = ((EditText)rootView.findViewById(R.id.editTextTitle)).getText().toString();
            note.theme =((EditText)rootView.findViewById(R.id.editTextTopic)).getText().toString();
            note.text = ((EditText)rootView.findViewById(R.id.editTextNote)).getText().toString();
            note.rate = ((RatingBar)rootView.findViewById(R.id.ratingBar)).getRating();

            DataBase.getInstance().delete(note.getId());
            DataBase.getInstance().add(note);

            Toast.makeText(getActivity(), getString(R.string.textNoteModified), Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Bundle args = getArguments();
        Note note = (Note) args.getSerializable("note");


        mMap = googleMap;
        LatLng defaultLocation = new LatLng(note.getCoordY(), note.getCoordX());


        currentMarker = mMap.addMarker(new MarkerOptions().position(defaultLocation).title(getString(R.string.textLocation)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        note.setCoordX(defaultLocation.longitude);
        note.setCoordY(defaultLocation.latitude);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.textNewLabel)));
                note.setCoordX(latLng.longitude);
                note.setCoordY(latLng.latitude);
            }
        });

    }

}
