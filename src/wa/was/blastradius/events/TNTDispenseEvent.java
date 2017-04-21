package wa.was.blastradius.events;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dispenser;
import org.bukkit.util.Vector;

import wa.was.blastradius.BlastRadius;
import wa.was.blastradius.managers.TNTEffectsManager;

/*************************
 * 
 *	Copyright (c) 2017 Jordan Thompson (WASasquatch)
 *	
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *	
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *	
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *	
 *************************/

public class TNTDispenseEvent implements Listener {

	private TNTEffectsManager TNTEffects;
	
	public TNTDispenseEvent() {
		TNTEffects = BlastRadius.getBlastRadiusInstance().getTNTEffectsManager();
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDispense(BlockDispenseEvent e) {
		if ( e.isCancelled() ) return;
		
		Block block = e.getBlock();
		Dispenser dispenser = (Dispenser)e.getBlock().getState().getData();
		ItemStack item = e.getItem();
		org.bukkit.block.Dispenser rdisp = (org.bukkit.block.Dispenser) e.getBlock().getState();
		
		if ( item != null && item.hasItemMeta() ) {
		
			ItemMeta meta = item.getItemMeta();
			
			if ( TNTEffects.hasDisplayName(meta.getDisplayName()) ) {
				
				e.setCancelled(true);
				
				String type = TNTEffects.displayNameToType(meta.getDisplayName());
				Map<String, Object> effect = TNTEffects.getEffect(type);
			
				BlockFace front = dispenser.getFacing();
				Location location = block.getRelative(front, 1).getLocation();
				Vector direction = new Vector(front.getModX(), front.getModY(), front.getModZ());
				
				rdisp.getInventory().removeItem(item);
				
				TNTEffects.createPrimedTNT(effect, 
						location, 
						(float) effect.get("yieldMultiplier"), 
						(int) effect.get("fuseTicks"), 
						(Sound) effect.get("fuseEffect"), 
						(float) effect.get("fuseEffectPitch"),
						(float) effect.get("fuseEffectPitch"),
						direction.normalize().multiply(0.1));
				
			}
			
		}
		
	}
	
	public ItemStack removeItem(ItemStack item) {
		if ( item.getAmount() > 1 ) {
			item.setAmount(item.getAmount() - 1);
		} else {
			item = null;
		}
		return item;
	}

}