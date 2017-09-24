package loordgek.loordcore.Register;

public interface IVariantLookup {
  default String[] variantnames(){
      return new String[]{"inventory"};
  }
}
