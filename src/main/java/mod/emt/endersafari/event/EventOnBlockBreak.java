package mod.emt.endersafari.event;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.config.ESConfig;
import mod.emt.endersafari.entity.EntityDireCube;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// TODO: Make a configurable list? There should also be a new config option to make it not drop anything when spawned this way
// Spawn Khndrel Keghts when using no tool or the wrong tool on a specific block
@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class EventOnBlockBreak {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            IBlockState state = event.getState();
            ItemStack heldStack = event.getPlayer().getHeldItemMainhand();
            if (!event.getPlayer().capabilities.isCreativeMode && isValidBlock(state) && !isValidStack(state, heldStack) && world.rand.nextFloat() <= ESConfig.ENTITIES.DIRE_CUBE.hardcoreSpawningChance) {
                BlockPos pos = event.getPos();
                EntityDireCube cube = new EntityDireCube(world);

                if (BiomeDictionary.hasType(cube.getEntityWorld().getBiome(cube.getPosition()), BiomeDictionary.Type.MESA)) {
                    cube.setType(3); // Red Sand
                } else if (BiomeDictionary.hasType(cube.getEntityWorld().getBiome(cube.getPosition()), BiomeDictionary.Type.SANDY)) {
                    cube.setType(2); // Sand
                } else if (BiomeDictionary.hasType(cube.getEntityWorld().getBiome(cube.getPosition()), BiomeDictionary.Type.SNOWY) || BiomeDictionary.hasType(cube.getEntityWorld().getBiome(cube.getPosition()), BiomeDictionary.Type.MOUNTAIN)) {
                    cube.setType(1); // Gravel
                } // Otherwise just Dirt

                cube.setSlimeSize(1, true);
                cube.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                world.spawnEntity(cube);
                cube.playLivingSound();
            }
        }
    }

    public static boolean isValidBlock(IBlockState state) {
        return state.getMaterial().isSolid() && state.isFullCube();
    }

    public static boolean isValidStack(IBlockState state, ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (String toolName : stack.getItem().getToolClasses(stack)) {
            if (state.getBlock().isToolEffective(toolName, state)) {
                return true;
            }
        }
        return false;
    }
}
