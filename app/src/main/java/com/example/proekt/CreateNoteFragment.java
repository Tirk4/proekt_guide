package com.example.proekt;

import androidx.activity.OnBackPressedCallback;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class CreateNoteFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View rootView;
    private ImageButton createNoteButton;
    private static double x;
    private static double y;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_create_note, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createNoteButton = rootView.findViewById(R.id.saveReadButton);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);

                Note noteObject = new Note(
                        ((RatingBar) rootView.findViewById(R.id.ratingBar)).getRating(),
                        ((EditText) rootView.findViewById(R.id.editTextTitle)).getText().toString(),
                        ((EditText) rootView.findViewById(R.id.editTextTopic)).getText().toString(),
                        ((EditText) rootView.findViewById(R.id.editTextNote)).getText().toString(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                        x,
                        y
                );
                DataBase.getInstance().add(noteObject);
                startActivity(intent);
                Toast.makeText(getActivity(), getString(R.string.textNoteSaved), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng defaultLocation = new LatLng(51.5074, -0.1278);
        x=defaultLocation.longitude;
        y=defaultLocation.latitude;
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title(getString(R.string.textLocation)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.textLocation)));
                x=latLng.longitude;
                y=latLng.latitude;

            }
        });

    }
}
