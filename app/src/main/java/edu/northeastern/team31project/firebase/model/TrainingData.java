package edu.northeastern.team31project.firebase.model;



public class TrainingData {
    public String equipment;
    public String instruction;
    public String muscle;
    public String time;
    public String training_name;


    public boolean matched(String selectedMuscle,
                           String selectedTime,
                           String selectedEquipment) {
        if (selectedMuscle != null && !selectedMuscle.equals(muscle)) {
            return false;
        }
        if (selectedTime != null && !selectedTime.equals(time)) {
            return false;
        }
        if (selectedEquipment != null &&
            selectedEquipment.equals("Yes") &&
            !equipment.equals("none")) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "TrainingData {" +
                "\nequipment='" + equipment + "'," +
                "\ninstruction='" + instruction + "'," +
                "\nmuscle='" + muscle + "'," +
                "\ntime= '" + time + "'," +
                "\ntraining_name='" + training_name +
                "\n}";
    }
}
