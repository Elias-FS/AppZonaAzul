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

public final class ActivityHomeBinding implements ViewBinding {
  @NonNull
  private final LinearLayoutCompat rootView;

  @NonNull
  public final AppCompatButton btnOpenConsultarVeiculo;

  private ActivityHomeBinding(@NonNull LinearLayoutCompat rootView,
      @NonNull AppCompatButton btnOpenConsultarVeiculo) {
    this.rootView = rootView;
    this.btnOpenConsultarVeiculo = btnOpenConsultarVeiculo;
  }

  @Override
  @NonNull
  public LinearLayoutCompat getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnOpenConsultarVeiculo;
      AppCompatButton btnOpenConsultarVeiculo = ViewBindings.findChildViewById(rootView, id);
      if (btnOpenConsultarVeiculo == null) {
        break missingId;
      }

      return new ActivityHomeBinding((LinearLayoutCompat) rootView, btnOpenConsultarVeiculo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
