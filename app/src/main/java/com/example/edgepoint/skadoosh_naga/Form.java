package com.example.edgepoint.skadoosh_naga;

import android.os.Parcel;
import android.os.Parcelable;

public class Form implements Parcelable {
    private String  Barangay, Precinct, Name;

    public Form(String Barangay, String Precinct, String Name){
        this.Barangay = Barangay;
        this.Precinct = Precinct;
        this.Name = Name;
    }

    public String getBarangay() {return Barangay;}
    public String getPrecinct() {return Precinct;}
    public String getName() {return Name;}

    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){
        //write all properties to the parcle
        dest.writeString(Barangay);
        dest.writeString(Precinct);
        dest.writeString(Name);
    }

    //constructor used for parcel
    public Form(Parcel parcel){
        //read and set saved values from parcel
        Barangay = parcel.readString();
        Precinct = parcel.readString();
        Name = parcel.readString();
    }

    //creator - used when un-parceling our parcle (creating the object)
    public static final Creator<Form> CREATOR = new Creator<Form>(){

        @Override
        public Form createFromParcel(Parcel parcel) {
            return new Form(parcel);
        }

        @Override
        public Form[] newArray(int size) {
            return new Form[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }
}
