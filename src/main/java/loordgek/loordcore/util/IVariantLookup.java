package loordgek.loordcore.util;

public interface IVariantLookup {
  default String[] variantnames(){
      return new String[]{"inventory"};
  }
}
