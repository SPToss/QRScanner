package Domain;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sebastian on 22.01.2017.
 */
public class Network
{
    private Context _context;
    private ArrayList<InputNeuron> InputsNeuron = new ArrayList<>(8);
    private ArrayList<HiddentNeuron> HiddensNeuron = new ArrayList<>(46);
    private ArrayList<OutputNeuron> OutputsNeuron = new ArrayList<>(255);
    public Network(Context context){
        try
        {
            _context = context;
            for(int i= 0; i<8; i++){
                String key = PropertyReader.GetPropertyByName("InputNeuronVage" + i,_context);
                InputsNeuron.add(new InputNeuron(ConvertStringToArrayList(key)));
            }
            for(int i=0;i<46;i++){
                String key = PropertyReader.GetPropertyByName("HiddenNeuronVage" + i,_context);
                HiddensNeuron.add(new HiddentNeuron(ConvertStringToArrayList(key)));
            }
            for(int i = 0;i<255; i++){
                String key = PropertyReader.GetPropertyByName("OutputNeuronVage" + i,_context);
                OutputsNeuron.add(new OutputNeuron(ConvertStringToArrayList(key)));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public String GetCharFormSegment(ArrayList<Double> segments){
        for (InputNeuron input : InputsNeuron)
        {
            input.CalculateValue(segments);
        }

        for(HiddentNeuron hidden : HiddensNeuron)
        {
            hidden.CalculateValue(InputsNeuron);
        }

        for(OutputNeuron output : OutputsNeuron)
        {
            output.CalculateValue(HiddensNeuron);
        }
        Double max = 0.0;
        int iterator = 0;
        for(int i = 0; i<OutputsNeuron.size(); i++){
            double temp = OutputsNeuron.get(i).GetNeuronValue();
            if(temp > max){
                max = temp;
                iterator = i;
            }
        }
        return Character.toString((char) iterator);
    }

    private ArrayList<Double> ConvertStringToArrayList(String key){
        String[] temp = key.split(";");
        ArrayList<Double> result = new ArrayList<>(temp.length);
        for(String item : temp){
            result.add(Double.parseDouble(item));
        }
        return result;
    }
}
