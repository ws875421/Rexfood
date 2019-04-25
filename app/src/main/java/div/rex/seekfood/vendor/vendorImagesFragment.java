package div.rex.seekfood.vendor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import div.rex.seekfood.R;


public class vendorImagesFragment extends Fragment {
    private vendorImages vendorImages;

    public vendorImagesFragment() {

    }

    public static vendorImagesFragment newInstance(vendorImages vendorImages) {
        vendorImagesFragment fragment = new vendorImagesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("vendorImages", vendorImages);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vendorImages = (vendorImages) getArguments().getSerializable("vendorImages");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vdetail, container, false);


        ImageView ivLogo = view.findViewById(R.id.ivLogo);
        ivLogo.setImageBitmap(vendorImages.getBitmap());

        return view;
    }
}
