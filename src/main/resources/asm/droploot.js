function initializeCoreMod() {
    return {
        'droploot': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.block.BlockState',
                'methodName': 'func_215693_a', // getDrops
                'methodDesc': '(Lnet/minecraft/world/storage/loot/LootContext$Builder;)Ljava/util/List;'
            },
            'transformer': function(method) {
              print('[MysticTools]: Patching Minecraft\' BlockState#getDrops');

                var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
                var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

                var instructions = method.instructions;
                var lastInstruction = instructions.get(0);

                var newInstructions = new InsnList();

                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));

                newInstructions.add(ASM.buildMethodCall(
                    "binary404/mystictools/common/hooks/Hooks",
                    "fireBlockDropLootEvent",
                    "(Lnet/minecraft/world/storage/loot/LootContext$Builder;Lnet/minecraft/block/BlockState;)Ljava/util/List;",
                    ASM.MethodType.STATIC
                ));

                                newInstructions.add(new InsnNode(Opcodes.ARETURN));

                method.instructions.insertBefore(lastInstruction, newInstructions);

                return method;
            }
        }
     }
  }