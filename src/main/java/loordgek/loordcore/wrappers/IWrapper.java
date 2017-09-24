package loordgek.loordcore.wrappers;

public interface IWrapper<FROM, TO> {

     TO wrapTo(FROM from);
}
