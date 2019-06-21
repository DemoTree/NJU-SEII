package com.example.cinema.vo;
import com.example.cinema.po.PlacingRateByDate;
import java.util.Date;
public class PlacingRateByDateVO {
    private Date date;
    private Float placingRate;

    public PlacingRateByDateVO(PlacingRateByDate placingRateByDate){
        this.date=placingRateByDate.getDate();
        this.placingRate=placingRateByDate.getPlacingRate();
    }
    public PlacingRateByDateVO(){}
    public Date getDate(){ return date; }
    public void setDate(Date date){this.date=date;}
    public Float getPlacingRate(){return placingRate;}
    public void setPlacingRate(Float placingRate){this.placingRate=placingRate;}




}
