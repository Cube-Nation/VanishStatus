package net.minecraft.src;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class mod_VanishStatus extends BaseMod 
{
    
    boolean isVanished = false;

    public mod_VanishStatus() 
    {
    }

    public String getName() 
    {
        return "VanishStatus";
    }

    public String getVersion() 
    {
        return "v1.1";
    }

    public void load() 
    {
        ModLoader.setInGameHook(this, true, false);
        ModLoader.registerPacketChannel(this, "vanishStatus");

    }
    
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if((mc.inGameHasFocus || mc.currentScreen == null) && !mc.gameSettings.showDebugInfo && !mc.gameSettings.keyBindPlayerList.pressed)
        {
            
            int potionFakeIndex = 1337;
            boolean hasVanishPotion = mc.thePlayer.activePotionsMap.containsKey(potionFakeIndex);

            if (isVanished && !hasVanishPotion)
            {
                mc.thePlayer.activePotionsMap.put(potionFakeIndex, new VanishPotionEffect());
            }
            else if (!isVanished && hasVanishPotion)
            {
                mc.thePlayer.activePotionsMap.remove(potionFakeIndex);
            }
            
        }
        return true;
    }
    
    public static void sendMessage(String channel, String message) {
        Packet250CustomPayload plugin = new Packet250CustomPayload();
        plugin.channel = channel;
        plugin.data = message.getBytes();
        plugin.length = message.length();
        ModLoader.getMinecraftInstance().getSendQueue().addToSendQueue(plugin);
    }

    
    // ModLoader @ MC 1.2.5
    public void receiveCustomPacket(Packet250CustomPayload packet250custompayload)
    {
        if (packet250custompayload.channel.equalsIgnoreCase("vanishStatus"))
        {
            handleMCMessage(packet250custompayload.data);
        }
    }
    
    // ModLoader @ MC 1.3+
    public void clientCustomPayload(NetClientHandler clientHandler, Packet250CustomPayload packet250custompayload)
    {
        receiveCustomPacket(packet250custompayload);
    }
    
    private void handleMCMessage(byte[] message)
    {
        isVanished = message.length > 0 && message[0] == 0x01;
        System.out.println("[VanishStatus] Got vanish status: " + (isVanished ? "vanished" : "visible") );
    }
    
    // ModLoader @ MC 1.2.5
    public void serverConnect(NetClientHandler netclienthandler) {
        sendMessage("vanishStatus", "check");
    }

    // ModLoader @ MC 1.3+
    public void clientConnect(NetClientHandler netclienthandler) {
        serverConnect(netclienthandler);
    }

}
