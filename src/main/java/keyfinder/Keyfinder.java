package keyfinder;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.jtransforms.fft.FloatFFT_1D;


public class Keyfinder {


	public static String findKey(File selectedFile) {
		
		// Chargement des données audio depuis l'input
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(selectedFile);
		} catch (UnsupportedAudioFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int sampleRate = (int) audioInputStream.getFormat().getSampleRate();
		int sampleSize = audioInputStream.getFormat().getSampleSizeInBits() / 8;
		int channels = audioInputStream.getFormat().getChannels();
		int frameSize = sampleSize * channels;
		byte[] audioData = new byte[(int) (audioInputStream.getFrameLength() * frameSize)];
		try {
			audioInputStream.read(audioData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Transformation de Fourier
		FloatFFT_1D fft = new FloatFFT_1D(audioData.length / frameSize);
		float[] audioDataFloat = new float[audioData.length / 2];
		for (int i = 0; i < audioDataFloat.length; i++) {
			audioDataFloat[i] = ((audioData[2 * i + 1] << 8) | (audioData[2 * i] & 0xff)) / 32768.0f;
		}
		fft.realForward(audioDataFloat);

		// Calcul de l'amplitude pour chaque fréquence
		float[] amplitudes = new float[audioDataFloat.length / 2];
		for (int i = 0; i < amplitudes.length; i++) {
			amplitudes[i] = (float) Math.sqrt(audioDataFloat[2 * i] * audioDataFloat[2 * i] + audioDataFloat[2 * i + 1] * audioDataFloat[2 * i + 1]);
		}

		// Fréquence fondamentale
		float maxAmplitude = 0;
		int maxAmplitudeIndex = 0;
		for (int i = 0; i < amplitudes.length; i++) {
			if (amplitudes[i] > maxAmplitude) {
				maxAmplitude = amplitudes[i];
				maxAmplitudeIndex = i;
			}
		}
		float fundamentalFrequency = (float) maxAmplitudeIndex * sampleRate / audioDataFloat.length;

		// Récupération de la clé à partir de la fréquence fondamentale
		String[] noteNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
		int noteIndex = (int) Math.round(12.0 * Math.log(fundamentalFrequency / 440.0) / Math.log(2.0)) + 69;
		String noteName = noteNames[noteIndex % 12];

		// Récupération du mode
		float[] majorProfile = {1.0f, 1.0f, 0.5f, 1.0f, 1.0f, 1.0f, 0.5f, 1.0f, 1.0f, 0.5f, 1.0f, 1.0f};
		float[] minorProfile = {1.0f, 0.5f, 1.0f, 1.0f, 0.5f, 1.0f, 1.0f, 1.0f, 0.5f, 1.0f, 0.5f, 1.0f};
		float majorSum = 0;
		float minorSum = 0;
		for (int i = 0; i < 12; i++) {
			majorSum += majorProfile[i % 12] * amplitudes[(int) (i * fundamentalFrequency / sampleRate * audioDataFloat.length / 2)];
			minorSum += minorProfile[i % 12] * amplitudes[(int) (i * fundamentalFrequency / sampleRate * audioDataFloat.length / 2)];
		}
		String mode = majorSum > minorSum ? "Majeur" : "Mineur";
		//Return clé + mode 
		return noteName + " " + mode;
	}








}




