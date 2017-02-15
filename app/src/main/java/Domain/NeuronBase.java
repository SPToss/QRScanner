package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 27.01.2017.
 */
public abstract class NeuronBase
{
    public NeuronBase(ArrayList<Double> inputValues){
        InputVages = inputValues;
    }

    protected ArrayList<Double> InputVages;

    protected double NeuronValue;

    public double GetNeuronValue(){
        return NeuronValue;
    }

    protected double ActivationFunction(double value){
        return 1 / (1 + Math.pow(Math.E,-value));
    }


}
