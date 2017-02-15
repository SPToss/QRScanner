package Domain;

import java.util.ArrayList;

/**
 * Created by Sebastian on 27.01.2017.
 */
public class InputNeuron extends NeuronBase
{
    public InputNeuron(ArrayList<Double> vagesList){
        super(vagesList);
    }

    public void CalculateValue(ArrayList<Double> InputValues)
    {
        Double value = 0.0;
        for(int i = 0; i< InputVages.size(); i++ ){
            value += InputVages.get(i) * InputValues.get(i);
        }
        NeuronValue = ActivationFunction(value);
    }
}
