package uoc.ds.pr.model;

public class ParkingReservation extends Reservation {

    public ParkingReservation(Voyage voyage, String idVehicle) {
        super(voyage);
        super.setIdVehicle(idVehicle);
    }
}
