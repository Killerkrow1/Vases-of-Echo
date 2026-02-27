package dev.cxd.v_o_e.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.cxd.v_o_e.init.ModBlocks;
import dev.cxd.v_o_e.init.ModParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.SculkSensorBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.GameEvent.Emitter;

public class AncientVaseBlock extends HorizontalFacingBlock {

    public enum VasePart implements StringIdentifiable {
        LOWER, MIDDLE, UPPER;

        @Override
        public String asString() {
            return this.name().toLowerCase();
        }
    }

    public static final EnumProperty<VasePart> PART = EnumProperty.of("part", VasePart.class);

    private static final VoxelShape SMALL_SHAPE =
            VoxelShapes.cuboid(0.125, 0.0, 0.125, 0.875, 1.0, 0.875);

    private static final VoxelShape BIG_LOWER_SHAPE =
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    private static final VoxelShape BIG_UPPER_SHAPE =
            VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.625, 1.0);

    private static final VoxelShape TALL_LOWER_SHAPE =
            VoxelShapes.cuboid(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
    private static final VoxelShape TALL_MIDDLE_SHAPE =
            VoxelShapes.cuboid(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
    private static final VoxelShape TALL_UPPER_SHAPE =
            VoxelShapes.cuboid(0.1875, 0.0, 0.1875, 0.8125, 0.75, 0.8125);

    public AncientVaseBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(PART, VasePart.LOWER));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, PART);
    }

    private boolean isTallVase(Block block)  { return block == ModBlocks.TALL_ANCIENT_VASE;  }
    private boolean isBigVase(Block block)   { return block == ModBlocks.BIG_ANCIENT_VASE;   }
    private boolean isSmallVase(Block block) { return block == ModBlocks.SMALL_ANCIENT_VASE; }
    private boolean isAnyVase(Block block)   { return isTallVase(block) || isBigVase(block) || isSmallVase(block); }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(PART) != VasePart.LOWER) return true;
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState base = (BlockState) Objects.requireNonNull(super.getPlacementState(ctx));
        Block block = base.getBlock();
        BlockPos pos = ctx.getBlockPos();
        PlayerEntity player = ctx.getPlayer();

        if (player != null) {
            Box playerBox = player.getBoundingBox();

            if (isBigVase(block)) {
                Box lower = new Box(pos.getX(), pos.getY(), pos.getZ(),
                        pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
                Box upper = new Box(pos.getX(), pos.getY() + 1, pos.getZ(),
                        pos.getX() + 1, pos.getY() + 1.625, pos.getZ() + 1);
                if (playerBox.intersects(lower) || playerBox.intersects(upper)) return null;

                if (!ctx.getWorld().getBlockState(pos.up()).isAir()) return null;

            } else if (isTallVase(block)) {
                Box lower  = new Box(pos.getX() + 0.1875, pos.getY(),     pos.getZ() + 0.1875,
                        pos.getX() + 0.8125, pos.getY() + 1, pos.getZ() + 0.8125);
                Box middle = new Box(pos.getX() + 0.1875, pos.getY() + 1, pos.getZ() + 0.1875,
                        pos.getX() + 0.8125, pos.getY() + 2, pos.getZ() + 0.8125);
                Box upper  = new Box(pos.getX() + 0.1875, pos.getY() + 2, pos.getZ() + 0.1875,
                        pos.getX() + 0.8125, pos.getY() + 2.75, pos.getZ() + 0.8125);
                if (playerBox.intersects(lower) || playerBox.intersects(middle) || playerBox.intersects(upper)) return null;

                if (!ctx.getWorld().getBlockState(pos.up()).isAir()
                        || !ctx.getWorld().getBlockState(pos.up(2)).isAir()) return null;
            }
        }

        return base
                .with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(PART, VasePart.LOWER);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (world.isClient()) return;

        Block block = state.getBlock();
        if (isBigVase(block)) {
            world.setBlockState(pos.up(),  state.with(PART, VasePart.UPPER),  Block.NOTIFY_ALL);
        } else if (isTallVase(block)) {
            world.setBlockState(pos.up(),  state.with(PART, VasePart.MIDDLE), Block.NOTIFY_ALL);
            world.setBlockState(pos.up(2), state.with(PART, VasePart.UPPER),  Block.NOTIFY_ALL);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            VasePart part = state.get(PART);
            Block block = state.getBlock();

            if (isBigVase(block)) {
                BlockPos otherPos = (part == VasePart.LOWER) ? pos.up() : pos.down();
                clearPart(world, otherPos);
            } else if (isTallVase(block)) {
                switch (part) {
                    case LOWER  -> { clearPart(world, pos.up());   clearPart(world, pos.up(2));   }
                    case MIDDLE -> { clearPart(world, pos.down()); clearPart(world, pos.up());    }
                    case UPPER  -> { clearPart(world, pos.down()); clearPart(world, pos.down(2)); }
                }
            }

            BlockPos lowerPos = getLowerPos(pos, state);
            ServerWorld serverWorld = (ServerWorld) world;

            boolean hasSilkTouch = player.getMainHandStack().hasEnchantments()
                    && player.getMainHandStack().getEnchantments().toString().contains("silk_touch");

            if (!player.isCreative() && !hasSilkTouch) {
                String command = String.format(
                        "loot spawn %d %d %d loot minecraft:chests/ancient_city",
                        lowerPos.getX(), lowerPos.getY(), lowerPos.getZ()
                );
                serverWorld.getServer().getCommandManager().executeWithPrefix(
                        serverWorld.getServer().getCommandSource(), command
                );
            }

            alertSculkSensors(serverWorld, lowerPos, player);
            spawnCustomParticle(serverWorld, lowerPos);
            echoToNearbyVases(serverWorld, lowerPos, player);
            serverWorld.emitGameEvent(GameEvent.BLOCK_DESTROY, lowerPos, Emitter.of(player));
        }

        super.onBreak(world, pos, state, player);
    }

    private void echoToNearbyVases(ServerWorld serverWorld, BlockPos origin, PlayerEntity player) {
        List<BlockPos> nearbyVases = new ArrayList<>();

        for (BlockPos nearbyPos : BlockPos.iterate(origin.add(-8, -8, -8), origin.add(8, 8, 8))) {
            if (nearbyPos.equals(origin)) continue;
            BlockState nearbyState = serverWorld.getBlockState(nearbyPos);
            if (isAnyVase(nearbyState.getBlock()) && nearbyState.get(PART) == VasePart.LOWER) {
                nearbyVases.add(nearbyPos.toImmutable());
            }
        }

        for (BlockPos vasePos : nearbyVases) {
            serverWorld.emitGameEvent(GameEvent.BLOCK_DESTROY, vasePos, Emitter.of(player));
            alertSculkSensors(serverWorld, vasePos, player);
            spawnCustomParticle(serverWorld, vasePos);
        }
    }

    private void alertSculkSensors(ServerWorld serverWorld, BlockPos origin, PlayerEntity player) {
        for (BlockPos nearbyPos : BlockPos.iterate(origin.add(-15, -15, -15), origin.add(15, 15, 15))) {
            BlockState nearbyState = serverWorld.getBlockState(nearbyPos);
            if (nearbyState.isOf(Blocks.SCULK_SENSOR)) {
                SculkSensorBlockEntity sensorEntity = (SculkSensorBlockEntity) serverWorld.getBlockEntity(nearbyPos);
                if (sensorEntity != null) {
                    sensorEntity.getEventListener().forceListen(
                            serverWorld, GameEvent.BLOCK_DESTROY,
                            Emitter.of(player), origin.toImmutable().toCenterPos()
                    );
                }
            }
        }
    }

    private BlockPos getLowerPos(BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        return switch (state.get(PART)) {
            case UPPER  -> isTallVase(block) ? pos.down(2) : pos.down();
            case MIDDLE -> pos.down();
            case LOWER  -> pos;
        };
    }

    private void clearPart(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == this) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
        }
    }

    private void spawnCustomParticle(ServerWorld serverWorld, BlockPos pos) {
        serverWorld.spawnParticles(
                ModParticles.ECHO,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                1, 0.0, 0.0, 0.0, 0.01
        );
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        Block block = state.getBlock();
        VasePart part = state.get(PART);

        if (isSmallVase(block)) {
            return SMALL_SHAPE;
        } else if (isBigVase(block)) {
            return part == VasePart.UPPER ? BIG_UPPER_SHAPE : BIG_LOWER_SHAPE;
        } else if (isTallVase(block)) {
            return switch (part) {
                case LOWER  -> TALL_LOWER_SHAPE;
                case MIDDLE -> TALL_MIDDLE_SHAPE;
                case UPPER  -> TALL_UPPER_SHAPE;
            };
        }
        return VoxelShapes.fullCube();
    }
}