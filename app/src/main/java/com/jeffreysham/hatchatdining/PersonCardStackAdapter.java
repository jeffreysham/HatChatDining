package com.jeffreysham.hatchatdining;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardStackAdapter;

/**
 * Created by Jeffrey Sham on 2/12/2016.
 */
public class PersonCardStackAdapter extends CardStackAdapter{

    public PersonCardStackAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getCardView(int i, CardModel cardModel, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.person_card, viewGroup, false);
            assert view != null;
        }

        if (cardModel instanceof PersonCardModel) {
            PersonCardModel theCardModel = (PersonCardModel) cardModel;
            String name = theCardModel.getTitle();
            String desc = theCardModel.getDescription();
            int age = theCardModel.getAge();

            if (name != null) {
                String[] nameArray = name.split(" ");
                if (nameArray.length == 1) {
                    ((TextView) view.findViewById(R.id.person_initials_view1)).setText(name.charAt(0) + "");
                } else {
                    ((TextView) view.findViewById(R.id.person_initials_view1)).setText(nameArray[0].charAt(0) + "" + nameArray[nameArray.length-1].charAt(0));
                }

                ((TextView) view.findViewById(R.id.name_view1)).setText(name);
            }

            if (desc != null) {
                ((TextView) view.findViewById(R.id.description_view1)).setText(desc);
            }

            if (age > 0) {
                ((TextView) view.findViewById(R.id.age_view1)).setText(age + "");
            }
        }

        return view;
    }
}
