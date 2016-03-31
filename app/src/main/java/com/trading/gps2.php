<?php  
    include("params.inc");
    //$host='localhost:D:/projects/databases/dtr2007.fdb';
    $dbh=ibase_connect($host, $uname, $pass);
    if (!$dbh)
        exit;
 /*   $prop=$_POST['prop'];
    if (is_null($prop)){
    echo '<?xml version="1.0" encoding="utf-8"?>';    
    echo '<real_order_id>-2</real_order_id>';
    exit;
    }*/

    if (isset($HTTP_RAW_POST_DATA)) {
        $request_xml = $HTTP_RAW_POST_DATA;
    }else{
        echo '<?xml version="1.0" encoding="utf-8"?>';    
        echo '<real_order_id>-3</real_order_id>';
        exit;
    }  
    
    /*      $manager_id=$_POST['manager_id'];
                  if (is_null($manager_id))
                              exit;     */
                              
    /*$request_xml="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><gps><lat>38</lat><lng>38</lng></gps>";*/
    $dom = new DOMDocument;
    $dom->loadXML($request_xml);
    if (!$dom) {
        echo '<?xml version="1.0" encoding="utf-8"?>';    
        echo '<real_order_id>-2</real_order_id>';
        exit;
    }   
    $xml=simplexml_import_dom($dom);


            $qry_txt= <<< EOL
INSERT INTO GPS( IDMANAGER,  LAT, LNG) VALUES 
               (?,         ?,     ?);           
EOL;
            $sth=ibase_query($dbh,$qry_txt,$xml->manager_id,$xml->lat,$xml->lng);
            if (!$sth)
            {
                echo '<?xml version="1.0" encoding="utf-8"?>';    
                echo '<real_order_id>-1</real_order_id>';
                exit;
            }
            echo '<?xml version="1.0" encoding="utf-8"?>';    
            echo "<real_order_id>1</real_order_id>"; 
    
?>
  
   