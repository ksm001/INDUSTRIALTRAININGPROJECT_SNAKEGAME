public class soundtest{
    public static void main(String[] args) {
        try {
            String soundName = "blip.wav";    
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            } 
            catch (Exception q) {}
            }
    }