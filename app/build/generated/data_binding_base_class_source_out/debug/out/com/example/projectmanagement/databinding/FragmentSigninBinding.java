// Generated by view binder compiler. Do not edit!
package com.example.projectmanagement.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.example.projectmanagement.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentSigninBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AppCompatButton btnSignIn;

  @NonNull
  public final LottieAnimationView buttonAnimation;

  @NonNull
  public final AppCompatEditText etEmail;

  @NonNull
  public final AppCompatEditText etPasswordLogin;

  @NonNull
  public final Toolbar toolbarSignUpActivity;

  @NonNull
  public final TextView tvTitle;

  private FragmentSigninBinding(@NonNull LinearLayout rootView, @NonNull AppCompatButton btnSignIn,
      @NonNull LottieAnimationView buttonAnimation, @NonNull AppCompatEditText etEmail,
      @NonNull AppCompatEditText etPasswordLogin, @NonNull Toolbar toolbarSignUpActivity,
      @NonNull TextView tvTitle) {
    this.rootView = rootView;
    this.btnSignIn = btnSignIn;
    this.buttonAnimation = buttonAnimation;
    this.etEmail = etEmail;
    this.etPasswordLogin = etPasswordLogin;
    this.toolbarSignUpActivity = toolbarSignUpActivity;
    this.tvTitle = tvTitle;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSigninBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSigninBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_signin, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSigninBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_sign_in;
      AppCompatButton btnSignIn = ViewBindings.findChildViewById(rootView, id);
      if (btnSignIn == null) {
        break missingId;
      }

      id = R.id.button_animation;
      LottieAnimationView buttonAnimation = ViewBindings.findChildViewById(rootView, id);
      if (buttonAnimation == null) {
        break missingId;
      }

      id = R.id.et_email;
      AppCompatEditText etEmail = ViewBindings.findChildViewById(rootView, id);
      if (etEmail == null) {
        break missingId;
      }

      id = R.id.et_password_login;
      AppCompatEditText etPasswordLogin = ViewBindings.findChildViewById(rootView, id);
      if (etPasswordLogin == null) {
        break missingId;
      }

      id = R.id.toolbar_sign_up_activity;
      Toolbar toolbarSignUpActivity = ViewBindings.findChildViewById(rootView, id);
      if (toolbarSignUpActivity == null) {
        break missingId;
      }

      id = R.id.tv_title;
      TextView tvTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvTitle == null) {
        break missingId;
      }

      return new FragmentSigninBinding((LinearLayout) rootView, btnSignIn, buttonAnimation, etEmail,
          etPasswordLogin, toolbarSignUpActivity, tvTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}