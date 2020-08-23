package com.adit.carnage.fragments;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adit.carnage.R;
import com.adit.carnage.adapters.ImageAdapter;
import com.adit.carnage.classes.Camera2Utility;
import com.adit.carnage.classes.Pictures;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @BindView(R.id.tvChatFragment)
    TextView tvChatFragment;

    @BindView(R.id.tv00)
    TextView tv00;

    @BindView(R.id.tv01)
    TextView tv01;

    @BindView(R.id.tv02)
    TextView tv02;

    @BindView(R.id.tv03)
    TextView tv03;

    @BindView(R.id.tv04)
    TextView tv04;

    @BindView(R.id.tv05)
    TextView tv05;

    @BindView(R.id.tv06)
    TextView tv06;

    @BindView(R.id.etArg)
    EditText etArg;

    @BindView(R.id.camPreview)
    TextureView camPreview;

    @BindView(R.id.mediaRv)
    RecyclerView mediaRv;

    @BindView(R.id.tvFileCount)
    TextView tvFileCount;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String result = "";
    private Camera2Utility camera2Utility;
    private boolean isRecording = false;

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        camera2Utility = new Camera2Utility(this, camPreview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        scanText("Welcome to HackerRank's Java tutorials!");
        //camera2Utility.prepareRecording();
//        fungsi00();
//        fungsi01("This website is for losers LOL!");
//        fungsi02(5);
//        fungsi03("+", 2, 2);
        scanFiles();

    }

    @Override
    public void onPause() {
        super.onPause();
        camera2Utility.closeCamera();
        camera2Utility.stopBackgroundThread();
        Toast.makeText(getContext(), "Fragment paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), "Fragment resumed", Toast.LENGTH_SHORT).show();
        camera2Utility.startBackgroundThread();
        if(camPreview.isAvailable()){
            camera2Utility.openCamera(camPreview.getWidth(), camPreview.getHeight());
        }else{
            camPreview.setSurfaceTextureListener(camera2Utility.mSurfaceTextureListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getContext(), "Fragment destroyed", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnStartService)
    public void capturePicture(){
        camera2Utility.takePicture();
    }

    @OnClick(R.id.btnStartRecording)
    public void record(){
        if(!isRecording()){
            camera2Utility.prepareRecording();
            setRecording(true);
        }else if(isRecording()){
            camera2Utility.stopRecording();
            setRecording(false);
        }

    }

    @OnClick(R.id.btnNav0)
    public void goToNavComponent00(){
        //NavDirections action = FragmentSatuDirections.chatFragmentAction();
        //NavHostFragment.findNavController(this).navigate(action);
    }

    @OnClick(R.id.btnNav1)
    public void goToNavComponent01(){
        String asdasd = etArg.getText().toString();
//        NavDirections action = FragmentSatuDirections.fragmentDuaAction();
        //FragmentSatuDirections.FragmentDuaAction navAction = FragmentSatuDirections.fragmentDuaAction();
        //navAction.setTitle(asdasd);

        //NavHostFragment.findNavController(this).navigate(navAction);
    }

    @OnClick(R.id.btnNav2)
    public void goToNavComponent02(){
        //NavDirections action = FragmentSatuDirections.contactsFragmentAction();
        //NavHostFragment.findNavController(this).navigate(action);
    }

    public void requestReadFileOnInternalStorage(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    public void requestWriteFileOnInternalStorage(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void setupRV(String layout){
        mediaRv.setHasFixedSize(true);

        if(layout.equalsIgnoreCase("linear")){
            layoutManager = new LinearLayoutManager(getContext());
        }else if(layout.equalsIgnoreCase("grid")) {
            layoutManager = new GridLayoutManager(getContext(), 3);
        }

        mediaRv.setLayoutManager(layoutManager);
        mediaRv.setAdapter(adapter);
    }

    private void scanFiles(){
//        String path = MediaStore.
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestReadFileOnInternalStorage();
            return;
        }

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestWriteFileOnInternalStorage();
            return;
        }

        List<Pictures> picturesList = new ArrayList<Pictures>();

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
        };

        String selection = "";
        String selectionArgs[] = new String[]{

        };

        // by name
        //String sortOrder = MediaStore.Images.Media.DISPLAY_NAME + " ASC";

        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " ASC";

        try (Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
            )){
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

            while(cursor.moveToNext()){
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int size = cursor.getInt(sizeColumn);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                picturesList.add(new Pictures(contentUri, name, size));
                File file = new File(contentUri.getPath());
            }
        }

        adapter = new ImageAdapter(picturesList);
        Toast.makeText(getContext(), picturesList.size() + " ", Toast.LENGTH_SHORT).show();
        tvFileCount.setText("TOTAL number of image files : " + picturesList.size());
        //setupRV("grid");
        setupRV("linear");
    }

    private void dumpFunctions(){
        fungsi00();
        fungsi01("This website is for losers LOL!");
        fungsi02(5);
        fungsi03("+", 2, 2);
        fungsi04(4, "Hai ");
//        fungsi05(new int[] {20,1,-1,2,-2,3,3,5,5,1,2,4,20,4,-1,-2,5} , tv04);
//        fungsi05(new int[] {1,1,2,-2,5,2,4,4,-1,-2,5}, tv05);
//        fungsi05(new int[] {20,1,1,2,2,3,3,5,5,4,20,4,5}, tv04);
        fungsi05(new int[] {10}, tv05);
        fungsi06("How can mirrors be real if our eyes aren't real");
//        fungsi06("most trees are blue");

//        fungsi05(new int[] {5,4,3,2,1,5,4,3,2,10,10}, tv04);
        getSum(1, 0);
        getSum(1, 2);
        getSum(0, 1);
        getSum(1, 1);
        getSum(-1, 0);
        getSum(-1, 2);

        whoLikesIt(new String[]{"Alex"});
        whoLikesIt(new String[]{"Alex", "Jacob"});
        whoLikesIt(new String[]{"Alex", "Jacob", "Mark"});
        whoLikesIt(new String[]{"Alex", "Jacob", "Mark", "Max"});
        whoLikesIt(new String[]{"Alex", "Jacob", "Mark", "Max", "Khoirul"});
//        persistence(39);
        persistence(999);
        high("man i need a taxi up to ubud");
        expandedForm(12);
        expandedForm(42);
        expandedForm(70304);
        expandedForm(900004);
        expandedForm(903040);
        expandedForm(900000);
        findLongest(new int[]{-10, 0, 1, 2, 10});
        findLongest(new int[]{-1172310, -91823, -823, 0, 1172310});
        findLongest(new int[]{1, 823, 8223, 39});
        findLongest(new int[]{-511270420, -1388418464});

        testTrim(new int[]{-1172310, -91823, -823, 0, 1172310});
        printDiamond(4);
        printDiamond(3);

        presses("LOL");
        presses("HOW R U");
        presses("WHERE DO U WANT 2 MEET L8R");
        presses("VJZ*CNLx@RNs#TcgfQIhS");

        rot("fijuoo\nCqYVct\nDrPmMJ\nerfpBA\nkWjFUG\nCVUfyL");


//        "(ARNO, ANN)(BELL, JOHN)(CORNWELL, ALEX)(DORNY, ABBA)(KERN, LEWIS)(KORN, ALEX)(META, GRACE)(SCHWARZ, VICTORIA)(STAN, MADISON)(STAN, MEGAN)(WAHL, ALEXIS)"
        meeting("Alexis:Wahl;John:Bell;Victoria:Schwarz;Abba:Dorny;Grace:Meta;Ann:Arno;Madison:STAN;Alex:Cornwell;Lewis:Kern;Megan:Stan;Alex:Korn");
    }

    private void fungsi00(){
        int num = 10;
        result = "";

        do{
            for(int i=num; i > 0; i--){
                result += String.valueOf(i);
            }
            num--;
            result += "\n";
        }while( num != 0);

        tvChatFragment.setText(result);
    }

    private void fungsi01(String words){
        char[] vowels = {'a', 'i', 'u', 'e','o'};
        String newString = words;

        for(int i = 0; i < words.length() ; i++){
            for(int j = 0; j < vowels.length ; j++){
                if(String.valueOf(words.charAt(i)).equalsIgnoreCase(String.valueOf(vowels[j]))){
                    newString = newString.replace(String.valueOf(words.charAt(i)), "");
                }
            }
        }

        tv00.setText(newString);
    }

    private void fungsi02(int n){

        float i = 1;
        float j = 1;
        int k = 1;
        float l = 1;

        String result = "";
        String pecahan = "";
        pecahan += k+"+";

        while (k < n){
            j += 3;
            i += (l/j);
            k++;

            pecahan += (int) l + "/" + (int) j + " + ";
        }

        result = String.format("%.2f", i);

        tv01.setText("n = "+ n + "\n" + pecahan + "\n" + result);
    }

    private void fungsi03(String operator, int a, int b){
        int result = 0;

        switch(operator){
            case "+":
               result = a + b;
               break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                result = a / b;
                break;
        }

        tv02.setText(a + operator + b + " = " + result);
    }

    private void fungsi04(int repeat, String string){
        String result = "";

        for(int i = 0 ; i < repeat; i++){
            result += string;
        }

        tv03.setText(result);
    }

    // https://www.codewars.com/kata/54da5a58ea159efa38000836/train/java
    private void fungsi05(int[] a, TextView tv){

        int odd = 0;
        int counter = 0;
        int finalNumber = 0;
        int finalCount = 0;

        int tampung = 0;

        // sorting
        for(int i = 0; i < a.length-1; i++){
            for(int j = 0; j < a.length-i-1; j++){
                if(a[j] > a[j+1]){
                    tampung = a[j];
                    a[j] = a[j+1];
                    a[j+1] = tampung;
                }
            }
        }

        if(a.length > 1){
            for(int i = 0; i < a.length; i++){
                if(a[i] != odd){
                    if(counter % 2 != 0){
                        finalNumber = odd;
                        finalCount = counter;
                    }
                    odd = a[i];
                    counter += 1;
                }else{
                    counter++;
                }
            }
        }
        else{
            finalNumber = a[0];
            finalCount = a.length;
        }


        tv.setText("Sorted Arrays = \n" + Arrays.toString(a) + "\nAngka " + finalNumber + " muncul sebanyak "+ finalCount +" kali");
    }

    private int[] sortArray(int[] a){

        int tampung = 0;
        int tampung2 = 0;

        for(int i = 0; i<a.length; i++){

//            do{
                if(i==0){
                    tampung = a[i];
                }else{
                    if(a[i] < tampung){
                        tampung2 = a[i];
                        a[i-1] = tampung2;
                        a[i] = tampung;
                    }
                }
//            }while(a[i] < a[i+1] );

        }
        return a;
    }

    //https://www.codewars.com/kata/5390bac347d09b7da40006f6/train/java
//    private void fungsi06(String phrase){
//        String[] split = phrase.split(" ");
//        String newJadenSentence = "";
//
//        for(int i = 0; i < split.length ; i++){
//            String word = split[i];
//            String firstLetter = String.valueOf(word.charAt(0));
//            word = firstLetter.replaceAll(firstLetter, firstLetter.toUpperCase());
//            newJadenSentence += word;
//        }
//        tv06.setText(phrase + "\n"+newJadenSentence);
//    }

    private void fungsi06(String phrase){
        int length = phrase.length();
        String newJadenSentence = "";

        String firstLetter = String.valueOf(phrase.charAt(0));
        newJadenSentence += firstLetter.replace(firstLetter, firstLetter.toUpperCase());

        for(int i = 1; i < length ; i++){
            String letter = String.valueOf(phrase.charAt(i));
//            newJadenSentence += letter;

            if(letter.equalsIgnoreCase(" ")){
                newJadenSentence += letter;
                String nextLetter = String.valueOf(phrase.charAt(i+1));
                nextLetter = nextLetter.replace(nextLetter, nextLetter.toUpperCase());
                newJadenSentence += nextLetter;
                i++;
            }else{
                newJadenSentence += letter;
            }
        }
        tv06.setText(phrase + "\n"+newJadenSentence);
    }

    private double[] fungsi07(double[] tribonacci, int n){
        double[] doubles = tribonacci;


        return doubles;
    }

    private int squareDigits(int n){
        String result = " ";
        String[] s = String.valueOf(n).split("");

        for(int i = 0; i < s.length ; i++){
            int num = Integer.valueOf(s[i]);
            result += num * num;
        }

        return Integer.valueOf(result);
    }

    public int getSum(int a, int b) {
        //Good luck!
        int result = 0;

        if (a == b) {
            result += a;
        } else {
            if (a > b) {
                for (int i = b; i <= a; i++) {
                    result += i;
                }
            } else {
                for (int i = a; i <= b; i++) {
                    result += i;
                }
            }
        }
        System.out.println(a + " + " + b + " = " + result);
        return result;
    }

    public static String whoLikesIt(String[] names) {
        //Do your magic here
        int jumlah = names.length;
        int cont = jumlah - 2;
        String result = "", likeThis = "like this";

        if(jumlah <= 0){
            result += "no one likes this";
        }else if(jumlah == 1){
            result += names[0] + " " + likeThis;
        }else if(jumlah == 2){
            result += names[0] + " and " + names[1] + " " + likeThis;
        }else if(jumlah == 3){
            result += names[0] + ", "+ names[1] + " and " + names[2] + " " + likeThis;
        }else if(jumlah >= 4){
            result += names[0] + ", "+ names[1] + " and " + cont + " others " + likeThis;
        }

        System.out.println(result);
        return result;
    }

    public static void persistence(long n) {
        // your code
        int persistence = 0;
        long tempNumber = n;
        String nString = String.valueOf(tempNumber);
        int result = Integer.valueOf(String.valueOf(nString.charAt(0)));

        while(nString.length() > 1){
            for(int i=1; i < nString.length(); i++) {
                int num = Integer.valueOf(String.valueOf(nString.charAt(i)));
                result = result * num;
            }
            tempNumber = result;
            nString = String.valueOf(tempNumber);
            result = Integer.valueOf(String.valueOf(nString.charAt(0)));
            persistence++;
        }

        System.out.println(tempNumber + " " + nString + " " + persistence);
//        return persistence;

    }

    public static String high(String s) {
        // Your code here...
        String[] newS = s.split(" ");
        int score = 0;
        int scoreTemp = 0;
        String theChoosenOne = "";

        for(int i=0; i<newS.length; i++){
            String word = newS[i];
            scoreTemp = 0;

            for(int j=0; j<word.length(); j++){
                String theChar = String.valueOf(word.charAt(j));
                int value = 0;

                switch(theChar){
                    case "a":
                        value = 1;
                        break;
                    case "b":
                        value = 2;
                        break;
                    case "c":
                        value = 3;
                        break;
                    case "d":
                        value = 4;
                        break;
                    case "e":
                        value = 5;
                        break;
                    case "f":
                        value = 6;
                        break;
                    case "g":
                        value = 7;
                        break;
                    case "h":
                        value = 8;
                        break;
                    case "i":
                        value = 9;
                        break;
                    case "j":
                        value = 10;
                        break;
                    case "k":
                        value = 11;
                        break;
                    case "l":
                        value = 12;
                        break;
                    case "m":
                        value = 13;
                        break;
                    case "n":
                        value = 14;
                        break;
                    case "o":
                        value = 15;
                        break;
                    case "p":
                        value = 16;
                        break;
                    case "q":
                        value = 17;
                        break;
                    case "r":
                        value = 18;
                        break;
                    case "s":
                        value = 19;
                        break;
                    case "t":
                        value = 20;
                        break;
                    case "u":
                        value = 21;
                        break;
                    case "v":
                        value = 22;
                        break;
                    case "w":
                        value = 23;
                        break;
                    case "x":
                        value = 24;
                        break;
                    case "y":
                        value = 25;
                        break;
                    case "z":
                        value = 26;
                        break;
                }

                scoreTemp += value;
            }

            if(scoreTemp > score){
                score = scoreTemp;
                theChoosenOne = word;
            }
        }

        System.out.println(theChoosenOne);
        return theChoosenOne;
        // ubud = 48
        // taxi = 54
    }


    // https://www.codewars.com/kata/550498447451fbbd7600041c/train/java
    public static boolean comp(int[] a, int[] b) {

        boolean result = false;
        int count = 1;
        String isiArrayA = "";
        String isiArrayB = "";

        System.out.println("Ukuran array a = " + a.length + "\nUkuran array b = " + b.length);

        for(int j=0; j<b.length; j++){
            int numberB = b[j];
            isiArrayB += numberB + ", ";
        }

        if(a != null || b != null || a.length > 0 || b.length > 0){
            for(int i=0; i<a.length; i++){
                result = false;
                int numberA = a[i];

                isiArrayA += numberA + ", ";

                int square = numberA * numberA;

                for(int j=0; j<b.length; j++){
                    int numberB = b[j];

                    if(square == numberB){
                        System.out.println("Angka b ke -"+j+" = "+ numberB + " sama dengan " + "Hasil square angka a ke-" + i + "( " + numberA + ") = " + square);
                        result = true;
                    }
                }
            }
            System.out.println("Isi array a = "+isiArrayA);
            System.out.println("Isi array b = "+isiArrayB);
        }

        return result;
    }

    // remove first char and last char of an string
    public static String remove(String str) {

        // your code here
        String after = "";
        String firstChar = String.valueOf(str.charAt(0));
        String lastChar = String.valueOf(str.charAt(str.length()-1));

        for(int i=1; i<str.length()-1; i++){
            after += String.valueOf(str.charAt(i));
        }
        System.out.println("first char = " + firstChar + "\nlast char = " + lastChar);

        return after;
    }

    // https://www.codewars.com/kata/5842df8ccbd22792a4000245/train/java
    public static String expandedForm(int num)
    {
        //your code here
        String numString = String.valueOf(num);
        String expand = "";
        String[] resultArray;
        List<String> resultList = new ArrayList<>();

        int decCounter = numString.length();

        for(int i=0; i<numString.length(); i++){
            String n = String.valueOf(numString.charAt(i));
            String inS = "", zeros = "";

            if(n.equals("0")){
                decCounter--;
            }else{
                for(int j=0; j< decCounter-1; j++){
                    zeros += "0";
                }
                inS += n + zeros;
                resultList.add(inS);
                decCounter--;
            }
        }

        for(int r=0; r<resultList.size(); r++){
            if(r== resultList.size() -1 ){
                expand += resultList.get(r);
            }else{
                expand += resultList.get(r) + "+";
            }
        }

        System.out.println("Num = "+ num +" => " + expand);
        return expand;
    }

    public static int findLongest(int[] numbers) {
        int longest = 0;

        for(int i=0; i< numbers.length; i++){
            String n = String.valueOf(numbers[i]);
            String newN = "";

            if(n.contains("-")){
                newN = n.replace("-", "");
            }else{
                newN = n;
            }

            System.out.println("n =" + n + "(" + n.length() +
                    ") newN ="+ newN + "(" + newN.length() + ")");

            if(newN.length() > String.valueOf(longest).replace("-", "").length()){
                longest = numbers[i];
            }
        }

        System.out.println("Longest = "+ longest);

        return longest;
    }

    public String testTrim(int[] n){

        String result = "";

        for(int i=0; i<n.length; i++){
            String a = String.valueOf(n[i]);
            String b = "";

            if(a.contains("-")){
                b = a.replace("-", "");
            }
            System.out.println("The number is : " + a +
                    "(" + a.length() + ") and the trimmed number is : " + b + "("+ b.length() + ")");

            result += a;
        }

        return result;
    }

    //https://www.codewars.com/kata/59df2f8f08c6cec835000012/train/java
    public static String meeting(String s) {
        // your code
        String result = "";
        String[] fullname = s.split(";");

        for(int i=0; i<fullname.length; i++){
            System.out.println("Fullname ke -" + i + " : "+ fullname[i]);

            String[] pair = fullname[i].split(":");
            for(int j=0; j<pair.length; j++){

                int charValue = 0;

                switch(String.valueOf(pair[j].charAt(0))){
                    case "A": charValue = 1; break;
                    case "B": charValue = 2; break;
                    case "C": charValue = 3; break;
                    case "D": charValue = 4; break;
                    case "E": charValue = 5; break;
                    case "F": charValue = 6; break;
                    case "G": charValue = 7; break;
                    case "H": charValue = 8; break;
                    case "I": charValue = 9; break;
                    case "J": charValue = 10; break;
                    case "K": charValue = 11; break;
                    case "L": charValue = 12; break;
                    case "M": charValue = 13; break;
                    case "N": charValue = 14; break;
                    case "O": charValue = 15; break;
                    case "P": charValue = 16; break;
                    case "Q": charValue = 17; break;
                    case "R": charValue = 18; break;
                    case "S": charValue = 19; break;
                    case "T": charValue = 20; break;
                    case "U": charValue = 21; break;
                    case "V": charValue = 22; break;
                    case "W": charValue = 23; break;
                    case "X": charValue = 24; break;
                    case "Y": charValue = 25; break;
                    case "Z": charValue = 26; break;
                }

            }

        }

        return result;
    }

    public static String printDiamond(int n) {
        // TODO your code here
        String result = "";

        if(n % 2 != 0 || n > 0){
            // vertikal
            for(int i=0; i<n; i++){

                //horizontal
                for(int j=0; j<n; j++){
                    if(j % 1 == 0 && j % 3 == 0){
                        result += "*";
                    }
                }
                result+= "\n";
            }
            System.out.println(result);
            return result;
        }
        else
            System.out.println("Input adalah bilangan genap !!1");
        return "Input adalah bilangan genap !!1";

    }

    // https://www.codewars.com/kata/54a2e93b22d236498400134b/train/java
    public static int presses(String phrase) {
        // hashmap0
        Map<String, Integer> letterMap = new HashMap<>();

        // hashmap1
        Map<Integer, String[]> letterMap1 = new HashMap<>();

        int totalPresses = 0;

        if(phrase == null || phrase.equals(" ")){
            return 0;
        }

        // method 1 : pakai hashmap
        letterMap.put("A", 1);
        letterMap.put("B", 2);
        letterMap.put("C", 3);
        letterMap.put("2", 4);

        letterMap.put("D", 1);
        letterMap.put("E", 2);
        letterMap.put("F", 3);
        letterMap.put("3", 4);

        letterMap.put("G", 1);
        letterMap.put("H", 2);
        letterMap.put("I", 3);
        letterMap.put("4", 4);

        letterMap.put("J", 1);
        letterMap.put("K", 2);
        letterMap.put("L", 3);
        letterMap.put("5", 4);

        letterMap.put("M", 1);
        letterMap.put("N", 2);
        letterMap.put("O", 3);
        letterMap.put("6", 4);

        letterMap.put("P", 1);
        letterMap.put("Q", 2);
        letterMap.put("R", 3);
        letterMap.put("S", 4);
        letterMap.put("7", 5);

        letterMap.put("T", 1);
        letterMap.put("U", 2);
        letterMap.put("V", 3);
        letterMap.put("8", 4);

        letterMap.put("W", 1);
        letterMap.put("X", 2);
        letterMap.put("Y", 3);
        letterMap.put("Z", 4);
        letterMap.put("9", 5);

        letterMap.put("1", 1);
        letterMap.put(" ", 1);

        for(int i=0; i<phrase.length(); i++){
            String theLetter = String.valueOf(phrase.charAt(i));
//            totalPresses += letterMap.get(String.valueOf(phrase.charAt(i)));
        }

        letterMap1.put(0, new String[]{" "});
        letterMap1.put(1, new String[]{"1"});
        letterMap1.put(2, new String[]{"a","b","c","2"});
        letterMap1.put(3, new String[]{"d","e","f","3"});
        letterMap1.put(4, new String[]{"g","h","i","4"});
        letterMap1.put(5, new String[]{"j","k","l","5"});
        letterMap1.put(6, new String[]{"m","n","o","6"});
        letterMap1.put(7, new String[]{"p","q","r","s","7"});
        letterMap1.put(8, new String[]{"t","u","v","8"});
        letterMap1.put(9, new String[]{"w","x","y","z","9"});
        letterMap1.put(10, new String[]{"*"});
        letterMap1.put(11, new String[]{"#"});

        for(int index=0; index<phrase.length(); index++){
            String theLetter = String.valueOf(phrase.charAt(index));

            for(int j : letterMap1.keySet()){
                String[] letterValue = letterMap1.get(j);

                for(int k=0; k< letterValue.length; k++){
                    String letterFromMap = letterValue[k];
                    if(theLetter.equalsIgnoreCase(letterFromMap)){
                        totalPresses += k+1;
                    }
                }
            }
        }

        System.out.println("Phrase : " +  phrase + " Total presses = " + totalPresses);
        return totalPresses;
    }

    public static String rot(String strng) {
        // your code
        String result = "";

        for(int i=strng.length()-1; i>=0; i--){
            String letter = String.valueOf(strng.charAt(i));
            result += letter;
        }
        System.out.println("Phrase : " + strng + " rot : " + result);
        return result;
    }

    // this function add .... to each substring ends with \n
    public static String selfieAndRot(String strng) {
        // your code
        String result = "";

        for(int i=strng.length()-1; i>=0; i--){
            String letter = String.valueOf(strng.charAt(i));
            result += letter;
        }

        System.out.println("Phrase : " + strng + " rot : " + result);
        return result;
    }
//    public static String oper(... operator, String s) {
//        // your code and complete ... before operator
//    }

    public void tesseract(){
        TessBaseAPI instance = new TessBaseAPI();
//        instance.setImage();
        String result = instance.getUTF8Text();
    }

    public void tensorFlow(){

    }

    public void scanText(String text){
        //Welcome to HackerRank's Java tutorials!
        Scanner scanner = new Scanner(text);
        String s = scanner.nextLine();
//        String s0 = scanner.next();
        showDialog("Debug", s + "\n");
    }

    public void showDialog(String title, String info){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        DialogInterface.OnClickListener a = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        dialog.setCancelable(true)
                .setTitle(title)
                .setMessage(info)
                .setNeutralButton("COK", a)
                .show();
    }

}
