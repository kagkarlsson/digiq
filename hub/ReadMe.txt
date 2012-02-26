
activemq.ny
->
forsendelser status=till_identifisering



forsendelser status=till_identifisering
-> 
paketer mottakersplitt
-> 
SFTP

SFTP
->
unpack kvittering
-> 
uppdatera forsendelse statusar (till_digipost, till_print, ikke_send)



forsendelser status=till_digi
->
digipost-client


forsendelser status=till_print
->
digipost-print