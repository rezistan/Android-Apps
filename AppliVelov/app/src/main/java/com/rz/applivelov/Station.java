package com.rz.applivelov;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sony Vaio on 25/03/2018.
 */

public class Station implements ClusterItem{
    private int number;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private boolean banking;
    private boolean bonus;
    private String status;
    private String contractName;
    private int bikeStands;
    private int availableBikeStands;
    private int availableBikes;
    private Date lastUpdate;

    Station(int number, String name, String address, double latitude, double longitude, boolean banking, boolean bonus, String status, String contractName, int bikeStands, int availableBikeStands, int availableBikes, Date lastUpdate) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.banking = banking;
        this.bonus = bonus;
        this.status = status;
        this.contractName = contractName;
        this.bikeStands = bikeStands;
        this.availableBikeStands = availableBikeStands;
        this.availableBikes = availableBikes;
        this.lastUpdate = lastUpdate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailableBikes() {
        return availableBikes;
    }

    @Override
    public String toString() {
        String bonusString;
        if(bonus) bonusString = "Oui";
        else bonusString = "Non";

        String bankingString;
        if (banking) bankingString = "Oui";
        else bankingString = "Non";

        if (status.equals("OPEN")) status = "En service";
        else status = "Hors service";


        @SuppressLint("SimpleDateFormat")
        DateFormat french = new SimpleDateFormat("dd/MM/yyyy à HH:mm");
        String maj = french.format(lastUpdate);

        return /*"\nNuméro : " + number +
                "\nName : " + name +*/
                "\nAdresse : " + address +
                /*
                "\n - latitude : " + latitude +
                "\n - longitude : " + longitude +
                */
                "\nVille : " + contractName +
                "\nNombre de Places : " + bikeStands +
                "\nPlaces Disponibles : " + availableBikeStands +
                "\nVélos Disponibles : " + availableBikes +

                "\n\nStatut : " + status +
                "\nSupport bancaire : " + bankingString +
                "\nBonus : " + bonusString +
                "\n\nActualisé le : " + maj +"\n";
    }

    public String available(){
        return "Vélos Disponibles : "+this.availableBikes;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(this.latitude, this.longitude);
    }

    @Override
    public String getTitle() {
        return this.name;
    }

    @Override
    public String getSnippet() {
        return this.available();
    }
}
