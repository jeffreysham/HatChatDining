package com.jeffreysham.hatchatdining;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardStackAdapter;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class RestaurantCardStackAdapter extends CardStackAdapter{

    private Context context;
    public RestaurantCardStackAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected View getCardView(int i, CardModel cardModel, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.restaurant_card, viewGroup, false);
            assert view != null;
        }

        if (cardModel instanceof RestaurantCardModel) {
            RestaurantCardModel theCardModel = (RestaurantCardModel) cardModel;
            String name = theCardModel.getTitle();
            String desc = theCardModel.getDescription();
            double rating = theCardModel.getRating();
            double distance = theCardModel.getDistance();
            String address = theCardModel.getAddress();
            String photoURL = theCardModel.getPhotoURL();

            if (photoURL != null) {
                Uri uri = Uri.parse(photoURL);
                SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);
                draweeView.setImageURI(uri);
            }

            if (name != null) {
                ((TextView) view.findViewById(R.id.rest_name_view)).setText(name);
            }

            if (desc != null) {
                ((TextView) view.findViewById(R.id.rest_description)).setText(desc);
            }

            if (rating > 0) {
                if (rating == 1) {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_1));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                } else if (rating == 1.5) {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_1));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_1_5));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                } else if (rating == 2) {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_2));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_2));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                } else if (rating == 2.5) {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_2));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_2));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_2_5));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                } else if (rating == 3) {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_3));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_3));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_3));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                } else if (rating == 3.5) {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_3));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_3));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_3));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_3_5));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                } else if (rating == 4) {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_0));
                } else if (rating == 4.5) {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_4_5));
                } else  {
                    ((ImageView) view.findViewById(R.id.star1)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_5));
                    ((ImageView) view.findViewById(R.id.star2)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_5));
                    ((ImageView) view.findViewById(R.id.star3)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_5));
                    ((ImageView) view.findViewById(R.id.star4)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_5));
                    ((ImageView) view.findViewById(R.id.star5)).setImageDrawable(context.getResources().getDrawable(R.drawable.normal_5));
                }
            }

            if (distance > 0) {
                double dist = distance * .000621371;

                String distString = String.format("%.2f miles away", dist);

                ((TextView) view.findViewById(R.id.distance_view)).setText(distString);
            }

            if (address != null) {
                ((TextView) view.findViewById(R.id.address_view)).setText(address);
            }
        }

        return view;
    }
}
