package son.nt.dota2.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import son.nt.dota2.R;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.facebook.UserDto;
import son.nt.dota2.utils.CommonUtil;

/**
 * Created by Sonnt on 1/19/16.
 */
public class SignUpFragment extends AFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String email;
    private String yourName;
    private String password;
    private String passwordConfirm;

    @BindView(R.id.sign_up_email_next)
    TextView txtNext;

    @BindView(R.id.sign_up_email)
    AppCompatEditText txtEmail;

    @BindView(R.id.sign_up_your_name)
    EditText txtYourName;

    @BindView(R.id.sign_up_password)
    AppCompatEditText txtPassword1St;

    @BindView(R.id.sign_up_password_confirm)
    AppCompatEditText txtPasswordConfirm;

    @BindView(R.id.sign_up_email_til)
    TextInputLayout textInputLayout;

    @BindView(R.id.sign_up_password1st_til)
    TextInputLayout textPassword1St;


    @BindView(R.id.sign_up_password1st_til_confirm)
    TextInputLayout textPasswordConfirm;


    @BindView(R.id.sign_up_email_Clp)
    ContentLoadingProgressBar contentLoadingProgressBar;

    @BindView(R.id.sign_up_ll_email)
    View viewEmail;
    @BindView(R.id.sign_up_ll_name)
    View viewYourName;
    @BindView(R.id.sign_up_ll_password)
    View viewPassword;
    @BindView(R.id.sign_up_ll_password_confirm)
    View viewPasswordConfirm;


    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            password = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.sign_up_email_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txtEmail.getText().toString().trim();
                if (!CommonUtil.isValidEmail(txtEmail.getText().toString().trim())) {
                    Toast.makeText(getActivity(), "ERROR !!!! Invalid Email.", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(password.trim()) || password.length() < 6) {
                    Toast.makeText(getActivity(), "ERROR !!!! Password is at less 6 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(getActivity(), getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
                    return;
                }

                UserDto userDto = new UserDto();
                userDto.email = txtEmail.getText().toString();
                userDto.setPassword(txtPasswordConfirm.getText().toString());
                userDto.setName(txtYourName.getText().toString());

            }
        });

        txtNext.setEnabled(false);

        txtEmail.addTextChangedListener(textWatcher);
        txtYourName.addTextChangedListener(textWatcherYourName);
        txtPassword1St.addTextChangedListener(textWatcherPassword1St);
        txtPasswordConfirm.addTextChangedListener(textWatcherPasswordConfirm);

    }

    private void checkEmailValid(String email) {
        if (!CommonUtil.isValidEmail(email)) {
            contentLoadingProgressBar.setVisibility(View.GONE);
            textInputLayout.setEnabled(true);
            textInputLayout.setError("Email invalid");
            txtNext.setEnabled(false);
            return;
        }



    }



    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            email = editable.toString();
            txtNext.setEnabled(false);


        }
    };


    TextWatcher textWatcherYourName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            yourName = editable.toString();
            txtNext.setEnabled(false);
            doCheckYourName();

        }
    };
    TextWatcher textWatcherPassword1St = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            password = editable.toString();
            txtNext.setEnabled(false);
            doCheckPassword1St();

        }
    };

    TextWatcher textWatcherPasswordConfirm = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            passwordConfirm = editable.toString();
            txtNext.setEnabled(false);
            doCheckPasswordConfirm();

        }
    };

    private void doCheckYourName() {
        txtNext.setEnabled(TextUtils.isEmpty(yourName.trim()) ? false : true);
    }

    private void doCheckPassword1St() {

        if (TextUtils.isEmpty(password.trim()) || password.length() < 6) {
            txtNext.setEnabled(false);
            textPassword1St.setEnabled(true);
            textPassword1St.setError(getString(R.string.error_less_than_6_letters));
        } else {
            txtNext.setEnabled(true);
            textPassword1St.setEnabled(false);
            textPassword1St.setError(null);
        }
    }

    private void doCheckPasswordConfirm() {

        if (password.equals(passwordConfirm)) {
            txtNext.setEnabled(true);
            textPasswordConfirm.setEnabled(false);
            textPasswordConfirm.setError(null);
            txtNext.setText(getString(R.string.create_account));
        } else {
            txtNext.setEnabled(false);
            textPasswordConfirm.setEnabled(true);
            textPasswordConfirm.setError(getString(R.string.password_not_match));
        }
    }


}
