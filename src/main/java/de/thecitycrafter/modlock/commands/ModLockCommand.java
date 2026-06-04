package de.thecitycrafter.modlock.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.thecitycrafter.modlock.attachments.AllowedModsData;
import de.thecitycrafter.modlock.attachments.Attachments;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.command.ModIdArgument;
import org.apache.logging.log4j.spi.CopyOnWrite;

public class ModLockCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("modlock")
                        .then(Commands.literal("add")
                                .then(Commands.argument("modid", ModIdArgument.modIdArgument())
                                        .executes(ctx -> {
                                            commandAdd(ctx);
                                            return 1;
                                        })
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .executes(ctx -> {
                                                    commandAdd(ctx);
                                                    return 1;
                                                }))))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("modid", ModIdArgument.modIdArgument())
                                        .executes(ctx -> {
                                            commandRemove(ctx);
                                            return 1;
                                        })
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .executes(ctx -> {
                                                    commandRemove(ctx);
                                                    return 1;
                                                }))))
                        .then(Commands.literal("has")
                                .then(Commands.argument("modid", ModIdArgument.modIdArgument())
                                        .executes(ModLockCommand::commandHas)
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .executes(ModLockCommand::commandHas))))
        );
    }

    private static void commandAdd(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();

        boolean hasTarget = ctx.getNodes().stream()
                .anyMatch(node -> node.getNode().getName().equals("target"));

        if (hasTarget){
            try {
                player = EntityArgument.getPlayer(ctx, "target");
            } catch (Exception ignored){
                player.sendSystemMessage(Component.translatable("modlock.command.message.player_not_found").withStyle(ChatFormatting.RED));
                return;
            }
        }
        AllowedModsData mods = player.getData(Attachments.ALLOWED_MODS);

        String modid = ctx.getArgument("modid", String.class);
        if (!mods.getMods().contains(modid)) {
            mods.add(modid);
            player.setData(Attachments.ALLOWED_MODS, mods);



            if (player.equals(ctx.getSource().getPlayer())) player.sendSystemMessage(Component.translatable("modlock.command.message.add.success", String.valueOf(modid)).withStyle(ChatFormatting.GREEN));
            else {
                ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.add.success.target", player.getName(), String.valueOf(modid)).withStyle(ChatFormatting.GREEN));
                player.sendSystemMessage(Component.translatable("modlock.command.message.add.success", String.valueOf(modid)).withStyle(ChatFormatting.GREEN));
            }

        }
        else  {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.add.fail", modid).withStyle(ChatFormatting.RED));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.add.fail.target", player.getName(), String.valueOf(modid)).withStyle(ChatFormatting.RED));
        }

    }

    private static void commandRemove(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();

        boolean hasTarget = ctx.getNodes().stream()
                .anyMatch(node -> node.getNode().getName().equals("target"));

        if (hasTarget){
            try {
                player = EntityArgument.getPlayer(ctx, "target");
            } catch (Exception ignored){
                player.sendSystemMessage(Component.translatable("modlock.command.message.player_not_found").withStyle(ChatFormatting.RED));
                return;
            }
        }
        AllowedModsData mods = player.getData(Attachments.ALLOWED_MODS);

        String modid = ctx.getArgument("modid", String.class);
        if (mods.getMods().contains(modid)) {
            mods.remove(modid);
            player.setData(Attachments.ALLOWED_MODS, mods);
            if (player.equals(ctx.getSource().getPlayer())) player.sendSystemMessage(Component.translatable("modlock.command.message.remove.success", String.valueOf(modid)).withStyle(ChatFormatting.GREEN));
            else {
                ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.remove.success.target", player.getName(), String.valueOf(modid)).withStyle(ChatFormatting.GREEN));
                player.sendSystemMessage(Component.translatable("modlock.command.message.remove.success", String.valueOf(modid)).withStyle(ChatFormatting.GREEN));
            }

        }
        else  {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.remove.fail", String.valueOf(modid)).withStyle(ChatFormatting.RED));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.remove.fail.target", player.getName(), String.valueOf(modid)).withStyle(ChatFormatting.RED));
        }
    }
    private static int commandHas(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();

        String modid = ctx.getArgument("modid", String.class);

        boolean hasTarget = ctx.getNodes().stream()
                .anyMatch(node -> node.getNode().getName().equals("target"));

        if (hasTarget){
            try {
                player = EntityArgument.getPlayer(ctx, "target");
            } catch (Exception ignored){
                player.sendSystemMessage(Component.translatable("modlock.command.message.player_not_found").withStyle(ChatFormatting.RED));
                return 0;
            }
        }

        AllowedModsData mods = player.getData(Attachments.ALLOWED_MODS);

        if (mods.getMods().contains(modid)) {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.has.success", String.valueOf(modid)).withStyle(ChatFormatting.GREEN));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.has.success.target", player.getName(), String.valueOf(modid)).withStyle(ChatFormatting.GREEN));
            return 10;
        }

        if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.has.fail", String.valueOf(modid)).withStyle(ChatFormatting.RED));
        else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.has.fail.target", player.getName(), String.valueOf(modid)).withStyle(ChatFormatting.RED));
        return 0;
    }
}
