// Generated by view binder compiler. Do not edit!
package br.com.appzonaazul.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import br.com.appzonaazul.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityConsultaVeiculoBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final AppCompatButton btnRegistrarIrregularidade;

  private ActivityConsultaVeiculoBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull AppCompatButton btnRegistrarIrregularidade) {
    this.rootView = rootView;
    this.btnRegistrarIrregularidade = btnRegistrarIrregularidade;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityConsultaVeiculoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityConsultaVeiculoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_consulta_veiculo, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityConsultaVeiculoBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnRegistrarIrregularidade;
      AppCompatButton btnRegistrarIrregularidade = ViewBindings.findChildViewById(rootView, id);
      if (btnRegistrarIrregularidade == null) {
        break missingId;
      }

      return new ActivityConsultaVeiculoBinding((LinearLayoutCompat) rootView,
          btnRegistrarIrregularidade);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
