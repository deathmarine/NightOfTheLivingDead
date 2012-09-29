package com.modcrafting.nightofthelivingdead.runnables;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.modcrafting.nightofthelivingdead.utils.Reversed;

public class StatsRunnable implements Runnable{
	NightOfTheLivingDead plugin;
	public StatsRunnable(NightOfTheLivingDead instance){
		plugin = instance;
	}
	Map<Player, Integer> level = new HashMap<Player, Integer>();
	@Override
	public void run() {
		plugin.getServer().broadcastMessage(ChatColor.BOLD+""+ChatColor.ITALIC+""+ChatColor.GREEN+"--- Current Stats of Players ---");
		for(Player player: plugin.getServer().getOnlinePlayers()){
			level.put(player, player.getLevel());
		}
		Map<Player, Integer> hm = sortByComparator(level);
		Collection<Player> cKey = hm.keySet();
		Iterator<Player> itrKey = cKey.iterator();
		int count = 0;
		while (itrKey.hasNext()){
			Player player = itrKey.next();
			String name = player.getName();
			StringBuilder sb = new StringBuilder();
			int size = name.length();
		    if(name.equalsIgnoreCase("deathmarin")){
		    	name = "Deathmarine";
		    }
			sb.append(name);
			if(size!=16){
				for(int i=0; i<(16-size); i++){
					sb.append(" ");
				}
			}
			if(count<=10){
				plugin.getServer().broadcastMessage(ChatColor.AQUA+sb.toString()+":"+ChatColor.BLUE+" Kills: "+String.valueOf(player.getLevel()));
				count = count+1;
			}
		}
		count = 0;
	    level.clear();
	    
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map<Player, Integer> sortByComparator(Map unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());
        Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
	           return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
             }
        });
        Reversed rev = new Reversed(list);
        Map sortedMap = new LinkedHashMap();
		for (Iterator it = rev.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
   }	
	
}
