

for mese in 01 02 03 04 05 06 07 08 09 10 11 12
do
   #echo $mese
   for giorno in 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
   do
      giorno_stop=""
      giorno_mese=$(echo $giorno$mese)
      
      if ( test $mese == "01" )
      then
          giorno_stop="31"
      elif ( test $mese == "02" )
      then
          giorno_stop="29"
      elif ( test $mese == "03" )
      then
          giorno_stop="31"
      elif ( test $mese == "04" )
      then
          giorno_stop="30"
      elif ( test $mese == "05" )
      then
          giorno_stop="31"
      elif ( test $mese == "06" )
      then
          giorno_stop="30"
      elif ( test $mese == "07" )
      then
          giorno_stop="31"
      elif ( test $mese == "08" )
      then
          giorno_stop="31"
      elif ( test $mese == "09" )
      then
          giorno_stop="30"
      elif ( test $mese == "10" )
      then
          giorno_stop="31"
      elif ( test $mese == "11" )
      then
          giorno_stop="30"
      elif ( test $mese == "12" )
      then
          giorno_stop="31"
      fi

      if ( test $giorno = $giorno_stop )
      then
            #echo "STOP DEL $mese -> $giorno_stop"
            echo "else if (strDateDay.equals(\"${giorno_mese}\"))"
            echo "     strText = getResources().getStringArray(R.array.giorno_${giorno_mese});"
            
            break;
      else
            echo "else if (strDateDay.equals(\"${giorno_mese}\"))"
            echo "     strText = getResources().getStringArray(R.array.giorno_${giorno_mese});"
            #echo "Scrivo il codice $giorno_mese"
      fi


   done

done
#if (strDateDay.equals("0101"))
#            strText = getResources().getStringArray(R.array.giorno_0708);
#else if (strDateDay.equals("0708"))
#            strText = getResources().getStringArray(R.array.giorno_0708);
#else if (strDateDay.equals("0808"))
#            strText = getResources().getStringArray(R.array.giorno_0808);
#else if (strDateDay.equals("3012"))
#            strText = getResources().getStringArray(R.array.giorno_0808);
#else // 31-12 default
#            strText = getResources().getStringArray(R.array.giorno_0808);
