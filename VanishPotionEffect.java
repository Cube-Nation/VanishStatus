package net.minecraft.src;

public class VanishPotionEffect extends PotionEffect {
	
	static int specialAmp = 3;

	public VanishPotionEffect() {
		super(Potion.invisibility.getId(), 0, specialAmp);
	}
	
    public String getEffectName()
    {
        return "Vanish";
    }
    
    public String toString() {
    	return "Vanish";
    }
    
    public int getAmplifier()
    {
        return specialAmp;
    }

}
