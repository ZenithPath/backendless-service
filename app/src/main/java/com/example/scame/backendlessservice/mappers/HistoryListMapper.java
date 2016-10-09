package com.example.scame.backendlessservice.mappers;


import com.example.scame.backendlessservice.models.HistoryAdapterItem;
import com.example.scame.backendlessservice.models.UserHistoryModel;
import com.example.scame.backendlessservice.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryListMapper {

    public List<HistoryAdapterItem> convert(List<UserModel> userModels) {
        List<HistoryAdapterItem> adapterItems = new ArrayList<>();

        for (UserModel userModel : userModels) {
            for (UserHistoryModel userHistoryModel : userModel.getLoginHistory()) {
                HistoryAdapterItem adapterItem = new HistoryAdapterItem();

                adapterItem.setName(userModel.getName());
                adapterItem.setDate(userHistoryModel.getCreated());
                adapterItem.setRegistrationType(userHistoryModel.getLoginType());

                adapterItems.add(adapterItem);
            }
        }

        return adapterItems;
    }
}
