package Domain;

import java.util.ArrayList;

/**
 * Created by Sebastian on 13.02.2017.
 */
public class HiddentNeuron extends NeuronBase
{
    public HiddentNeuron(ArrayList<Double> inputValues)
    {
        super(inputValues);
    }

    public void CalculateValue(ArrayList<InputNeuron> inputs)
    {
        Double value = 0.0;
        for(int i = 0; i< InputVages.size(); i++ ){
            value += InputVages.get(i) * inputs.get(i).GetNeuronValue();
        }
        NeuronValue = ActivationFunction(value);
    }
}
