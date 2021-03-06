package me.sethallen.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ImageConfiguration implements Parcelable {

    @JsonProperty("base_url")
    private String baseUrl;
    @JsonProperty("secure_base_url")
    private String secureBaseUrl;
    @JsonProperty("backdrop_sizes")
    private List<String> backdropSizes = new ArrayList<>();
    @JsonProperty("logo_sizes")
    private List<String> logoSizes = new ArrayList<>();
    @JsonProperty("poster_sizes")
    private List<String> posterSizes = new ArrayList<>();
    @JsonProperty("profile_sizes")
    private List<String> profileSizes = new ArrayList<>();
    @JsonProperty("still_sizes")
    private List<String> stillSizes = new ArrayList<>();
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     *
     * @param baseUrl
     * The base_url
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     *
     * @return
     * The secureBaseUrl
     */
    public String getSecureBaseUrl() {
        return secureBaseUrl;
    }

    /**
     *
     * @param secureBaseUrl
     * The secure_base_url
     */
    public void setSecureBaseUrl(String secureBaseUrl) {
        this.secureBaseUrl = secureBaseUrl;
    }

    /**
     *
     * @return
     * The backdropSizes
     */
    public List<String> getBackdropSizes() {
        return backdropSizes;
    }

    /**
     *
     * @param backdropSizes
     * The backdrop_sizes
     */
    public void setBackdropSizes(List<String> backdropSizes) {
        this.backdropSizes = backdropSizes;
    }

    /**
     *
     * @return
     * The logoSizes
     */
    public List<String> getLogoSizes() {
        return logoSizes;
    }

    /**
     *
     * @param logoSizes
     * The logo_sizes
     */
    public void setLogoSizes(List<String> logoSizes) {
        this.logoSizes = logoSizes;
    }

    /**
     *
     * @return
     * The posterSizes
     */
    public List<String> getPosterSizes() {
        return posterSizes;
    }

    /**
     *
     * @param posterSizes
     * The poster_sizes
     */
    public void setPosterSizes(List<String> posterSizes) {
        this.posterSizes = posterSizes;
    }

    /**
     *
     * @return
     * The profileSizes
     */
    public List<String> getProfileSizes() {
        return profileSizes;
    }

    /**
     *
     * @param profileSizes
     * The profile_sizes
     */
    public void setProfileSizes(List<String> profileSizes) {
        this.profileSizes = profileSizes;
    }

    /**
     *
     * @return
     * The stillSizes
     */
    public List<String> getStillSizes() {
        return stillSizes;
    }

    /**
     *
     * @param stillSizes
     * The still_sizes
     */
    public void setStillSizes(List<String> stillSizes) {
        this.stillSizes = stillSizes;
    }

//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.baseUrl);
        dest.writeString(this.secureBaseUrl);
        dest.writeStringList(this.backdropSizes);
        dest.writeStringList(this.logoSizes);
        dest.writeStringList(this.posterSizes);
        dest.writeStringList(this.profileSizes);
        dest.writeStringList(this.stillSizes);
        //dest.writeParcelable(this.additionalProperties, flags);
    }

    public ImageConfiguration() {
    }

    protected ImageConfiguration(Parcel in) {
        this.baseUrl = in.readString();
        this.secureBaseUrl = in.readString();
        this.backdropSizes = in.createStringArrayList();
        this.logoSizes = in.createStringArrayList();
        this.posterSizes = in.createStringArrayList();
        this.profileSizes = in.createStringArrayList();
        this.stillSizes = in.createStringArrayList();
        //this.additionalProperties = in.readParcelable(Map<String, Object>.class.getClassLoader());
    }

    public static final Parcelable.Creator<ImageConfiguration> CREATOR = new Parcelable.Creator<ImageConfiguration>() {
        public ImageConfiguration createFromParcel(Parcel source) {
            return new ImageConfiguration(source);
        }

        public ImageConfiguration[] newArray(int size) {
            return new ImageConfiguration[size];
        }
    };

    public String getClosestPosterSize(int preferredSizeInPixels)
    {
        List<Integer> posterSizeNumericList  = getIntegerList(getPosterSizes());
        Integer       closestPosterSize      = closestItemInList(preferredSizeInPixels, posterSizeNumericList);
        return "w" + closestPosterSize.toString();
    }

    public String getClosestBackdropSize(int preferredSizeInPixels)
    {
        List<Integer> backdropSizeNumericList  = getIntegerList(getBackdropSizes());
        Integer       closestBackdropSize      = closestItemInList(preferredSizeInPixels, backdropSizeNumericList);

        // If the preferred size is more than 50% larger than the closest size,
        // return the "original" image which is likely a better fit.
        if (preferredSizeInPixels > (closestBackdropSize * 1.5)
                && closestBackdropSize == backdropSizeNumericList.get(backdropSizeNumericList.size() - 1)) {
            return "original";
        } else {
            return "w" + closestBackdropSize.toString();
        }
    }

    private List<Integer> getIntegerList(List<String> oldList)
    {
        List<Integer> newList = new ArrayList<>(oldList.size());
        for (String stringValue : oldList) {
            if (!stringValue.equals("original")) {
                newList.add(Integer.parseInt(stringValue.substring(1)));
            }
        }
        return newList;
    }

    private int closestItemInList(Integer value, List<Integer> sorted) {
        if(value < sorted.get(0)) {
            return sorted.get(0);
        }

        int i = 1;
        for( ; i < sorted.size() && value > sorted.get(i) ; i++);

        if(i >= sorted.size()) {
            return sorted.get(sorted.size() - 1);
        }

        return Math.abs(value - sorted.get(i)) < Math.abs(value - sorted.get(i-1)) ?
                sorted.get(i) : sorted.get(i-1);
    }
}