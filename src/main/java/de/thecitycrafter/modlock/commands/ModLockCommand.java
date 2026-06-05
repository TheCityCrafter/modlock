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
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.command.ModIdArgument;

import java.util.List;

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

                        .then(Commands.literal("list")
                                .executes(ModLockCommand::commandList)
                                        .then(Commands.argument("target", EntityArgument.players())
                                                .executes(ModLockCommand::commandList)))

                        .then(Commands.literal("clear")
                                .executes(ctx -> {
                                    commandClear(ctx);
                                    return 1;
                                })
                                .then(Commands.argument("target", EntityArgument.players())
                                        .executes(ctx ->{
                                            commandClear(ctx);
                                            return 1;
                                        })))



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
                player.sendSystemMessage(Component.translatable("modlock.command.message.player_not_found"));
                return;
            }
        }
        AllowedModsData mods = player.getData(Attachments.ALLOWED_MODS);

        String modid = ctx.getArgument("modid", String.class);
        if (!mods.getMods().contains(modid)) {
            mods.add(modid);
            player.setData(Attachments.ALLOWED_MODS, mods);



            if (player.equals(ctx.getSource().getPlayer())) player.sendSystemMessage(Component.translatable("modlock.command.message.add.success", Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
            else {
                ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.add.success.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA), Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
                player.sendSystemMessage(Component.translatable("modlock.command.message.add.success", Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
            }

        }
        else  {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.add.fail", Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.add.fail.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA), Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
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
                player.sendSystemMessage(Component.translatable("modlock.command.message.player_not_found"));
                return;
            }
        }
        AllowedModsData mods = player.getData(Attachments.ALLOWED_MODS);

        String modid = ctx.getArgument("modid", String.class);
        if (mods.getMods().contains(modid)) {
            mods.remove(modid);
            player.setData(Attachments.ALLOWED_MODS, mods);
            if (player.equals(ctx.getSource().getPlayer())) player.sendSystemMessage(Component.translatable("modlock.command.message.remove.success", Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
            else {
                ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.remove.success.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA), Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
                player.sendSystemMessage(Component.translatable("modlock.command.message.remove.success",Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
            }

        }
        else  {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.remove.fail", Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.remove.fail.target", Component.literal(String.valueOf(Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA))).withStyle(ChatFormatting.AQUA), Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
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
                player.sendSystemMessage(Component.translatable("modlock.command.message.player_not_found"));
                return 0;
            }
        }

        AllowedModsData mods = player.getData(Attachments.ALLOWED_MODS);

        if (mods.getMods().contains(modid)) {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.has.success", Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.has.success.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA), Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
            return 10;
        }

        if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.has.fail", Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
        else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.has.fail.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA), Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA)));
        return 0;
    }

    private static int commandList(CommandContext<CommandSourceStack> ctx) {

        Player player = ctx.getSource().getPlayer();

        boolean hasTarget = ctx.getNodes().stream()
                .anyMatch(node -> node.getNode().getName().equals("target"));

        if (hasTarget){
            try {
                player = EntityArgument.getPlayer(ctx, "target");
            } catch (Exception ignored){
                player.sendSystemMessage(Component.translatable("modlock.command.message.player_not_found"));
                return 0;
            }
        }

        AllowedModsData mods = player.getData(Attachments.ALLOWED_MODS);


        if (player.hasData(Attachments.ALLOWED_MODS) && mods.getMods() != null && !mods.getMods().isEmpty()) {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.list.success"));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.list.success.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA)));

            for (String modid : mods.getMods()) {
                ctx.getSource().sendSystemMessage(Component.literal(String.valueOf(modid)).withStyle(ChatFormatting.AQUA));
            }
        }else {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.list.fail"));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.list.fail.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA)));
        }


        return mods.getMods().size();
    }

    private static void commandClear(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();

        boolean hasTarget = ctx.getNodes().stream()
                .anyMatch(node -> node.getNode().getName().equals("target"));

        if (hasTarget){
            try {
                player = EntityArgument.getPlayer(ctx, "target");
            } catch (Exception ignored){
                player.sendSystemMessage(Component.translatable("modlock.command.message.player_not_found"));
            }
        }

        AllowedModsData mods = player.getData(Attachments.ALLOWED_MODS);


        if (player.hasData(Attachments.ALLOWED_MODS) && mods.getMods() != null && !mods.getMods().isEmpty()) {
            if (player.equals(ctx.getSource().getPlayer())) {
                List<String> modList = mods.getMods();
                modList.clear();
                mods.set(modList);
                player.setData(Attachments.ALLOWED_MODS, mods);
                ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.clear.success"));

            }
            else {
                List<String> modList = mods.getMods();
                modList.clear();
                mods.set(modList);
                player.setData(Attachments.ALLOWED_MODS, mods);
                ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.clear.success.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA)));
            }

        }else {
            if (player.equals(ctx.getSource().getPlayer())) ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.clear.fail"));
            else ctx.getSource().sendSystemMessage(Component.translatable("modlock.command.message.clear.fail.target", Component.literal(String.valueOf(player.getName())).withStyle(ChatFormatting.AQUA)));
        }
    }
}
