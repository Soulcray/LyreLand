package generation;

import analysis.harmonic.ChordDegree;
import analysis.harmonic.Scale;
import analysis.harmonic.Tonality;
import training.probability.MarkovMatrix;
import training.probability.ProbabilityVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Harmonic {
    private Tonality tonality_;
    private MarkovMatrix<ChordDegree> markovDegree_;
    private ProbabilityVector<List<ChordDegree>> endings_;

    public Harmonic(Tonality tonality, MarkovMatrix<ChordDegree> markovDegree,
                    ProbabilityVector<List<ChordDegree>> endings) {
        tonality_ = tonality;
        markovDegree_ = markovDegree;
        endings_ = endings;
    }

    /**
     * This function generate an harmonic base using the MarkovMatrix<ChordDegree> transition
     * matrix.
     * @param barNumber number of bar we want to generate for the harmonic base.
     * @return an ArrayList of ChordDegree.
     */
    public ArrayList<ChordDegree> generateHarmonicBase(int barNumber, long seed) {
        ArrayList<ChordDegree> result = new ArrayList<>();
        ArrayList<ChordDegree> temp = new ArrayList<>();
        ChordDegree depth1 = null;
        ChordDegree depth2 = null;
        double sumDen = 0.0;
        Random generator = new Random(seed + 125);
        for(int i = 0; i < barNumber; ++i) {
            ChordDegree tempDepth1 = depth1;
            ChordDegree tempDepth2 = depth2;
            while (sumDen < 1.0) {
                ChordDegree newChord = markovDegree_.getRandomValue(Arrays.asList(tempDepth1, tempDepth2), generator);
                temp.add(newChord);
                sumDen += 1.0 / (double)newChord.getBarFractionDen();
                tempDepth1 = tempDepth2;
                tempDepth2 = newChord;
            }
            if (sumDen == 1.0) {
                result.addAll(temp);
                depth1 = tempDepth1;
                depth2 = tempDepth2;
            }
            else
                --i;
            sumDen = 0.0;
            temp.clear();

        }
        //List<ChordDegree> ending = endings_.getValue(generator);
        //result.addAll(ending);
        return result;
    }

    /**
     * This function takes a ChordDegree as Input and transform it into an Array of pitches
     * that can later be used to generate a CPhrase for instance.
     * @param degree
     * @param offset is an int value uses to calibrate the generate hight of the chord
     *               since every pitch in the scale is modulo 12 ...
     * @return an array of integer.
     */
    public int[] getChord(ChordDegree degree, int offset) {
        ArrayList<Integer> scale = new Scale(tonality_, 2).getScale();
        int value = degree.getDegree();
        if (!degree.isSeventhChord())
            return new int[]{scale.get(value) + offset, scale.get(value + 2) + offset,
                             scale.get(value + 4) + offset};
        else
            return new int[]{scale.get(value) + offset, scale.get(value + 2) + offset,
                             scale.get(value + 4) + offset, scale.get(value + 6) + offset};
    }
}
