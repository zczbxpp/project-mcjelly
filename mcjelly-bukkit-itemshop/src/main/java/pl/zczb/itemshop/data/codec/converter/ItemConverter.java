package pl.zczb.itemshop.data.codec.converter;

import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import pl.zczb.itemshop.data.codec.CodecHelper;
import pl.zczb.itemshop.data.codec.Converter;

import java.util.Map;

public class ItemConverter implements Converter<ItemStack> {
    public Document encode(ItemStack itemStack) {
        return new Document(itemStack.serialize());
    }


    public ItemStack decode(Document document, CodecHelper helper) {
        return ItemStack.deserialize((Map) document);
    }


    public Class<ItemStack> getConvertedClass() {
        return ItemStack.class;
    }
}


