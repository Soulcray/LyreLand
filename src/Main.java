import Analysis.DataWriter;
import Analysis.NormalizeRythm;
import Analysis.ScoreAnalyser;
import jm.music.data.*;
import jm.util.Play;
import jm.util.View;
import jm.util.Write;
import tonality.Scale;

import java.util.ArrayList;

public class Main {

    public static void demoScale(int tonic, String s, int octaveNumber, double rythm){
        Score score = new Score();
        Part p = new Part();
        Phrase phr = new Phrase();
        Scale scale = new Scale(tonic, s, octaveNumber);
        ArrayList<Integer> basicScale = scale.getScale();
        for (int i = 0; i < basicScale.size(); i++) {
            Note n = new Note(basicScale.get(i), rythm);
            System.out.println(basicScale.get(i));
            phr.add(n);
        }
        p.add(phr);
        score.add(p);
        Write.midi(score, "basic_scale.mid"); // create midiFile
        View.show(score);
        Play.mid("basic_scale.mid"); // Play the sound
    }

    public static void main(String[] args) {
        System.out.println("Hello LyreLand!");
        //demoScale(60, "major", 1, 0.5);

        ScoreAnalyser sa = new ScoreAnalyser("/home/olivier/Documents/LyreLand/Analysis_Music/Guren_no_Yumiya/Guren no Yumiya.mid");
        //ScoreAnalyser sa = new ScoreAnalyser("/home/olivier/Documents/LyreLand/Analysis_Music/Blue_Bird/Blue_Bird.mid");
        //sa.getScale().showScale();
        // Print the normalised notes
        // use normaliseRythm() if you don't want to print
        NormalizeRythm.normaliseRythm(sa.getScore());
        NormalizeRythm.normalisePhraseLength(sa.getScore());
        //ChordExtractor.sequenceChords(sa.getScore());
        //sa.checknpl();
        DataWriter dt = new DataWriter(sa);
        dt.writeData("toto.txt");
        //Write.midi(sa.getScore(), "foobar.mid");
        //Play.mid("foobar.mid");
    }
}