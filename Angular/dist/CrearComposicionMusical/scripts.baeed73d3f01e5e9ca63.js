const configuracionesProyecto={rutaWebApi:"http://localhost:8080/api"};function selectBuscador(e){var t="",l=e.params,n=null,o=null;if(null!=e.querySelector&&""!=e.querySelector)n=document.querySelector(e.querySelector);else{if(!(null!=e.objetoHTML&&e.objetoHTML instanceof Object&&"select"==e.objetoHTML.localName))return console.log("No se recibio un querySelector o un objetoHTML para saber que select ocupar en el buscador");n=e.objetoHTML}var a=n.parentElement,i=Array.from(a.childNodes).find(e=>null!=e.localName&&"input"==e.localName),r=null!=e.mensajeBuscando?e.mensajeBuscando:"buscando ...";function s(){null!=o&&n.appendChild(o)}if(null==n.getAttribute("pagina")&&(n.setAttribute("pagina","1"),n.setAttribute("estaCompleto","false")),null==e.optionSelected||["",null].includes(e.optionSelected.id)||["",null].includes(e.optionSelected.text))null!=(o=Array.from(n.options).find(e=>1==e.selected))&&(n.setAttribute("textoSeleccionado",o.text),n.setAttribute("valorSeleccionado",o.value),s());else{var u=document.createElement("option");u.text=e.optionSelected.text,u.value=e.optionSelected.id,u.selected=!0,o=u,n.setAttribute("textoSeleccionado",e.optionSelected.text),n.setAttribute("valorSeleccionado",e.optionSelected.id),s()}function d(l={}){l[e.registrosPorPagina.nombre]=e.registrosPorPagina.valor;var a=parseInt(n.getAttribute("pagina"));l[e.nombrePagina]=a,l[e.nombreBusqueda]=""!=t?t:null;var r="?"+Object.entries(l).map(e=>e.join("=")).join("&"),s=n.scrollTop;fetch(e.url+r,{method:"GET",headers:{Authorization:null!=e.token?"Bearer "+e.token:null}}).then(e=>e.json()).then(e=>{i.disabled=!1,n.disabled=!1,i.placeholder="",i.style.display="block",i.focus(),n.style.position="absolute",n.style.zIndex="3000",n.style.width=i.getBoundingClientRect().width+"px";var t=e.Results,l=e.Total;if(t.length>0)if(l>t.length){var r=n.size;n.size=t.length-1,n.size<r&&(n.size=r)}else n.size=t.length<=l?t.length+1:1==t.length&&1==a?2:t.length+1;if(null==i.value||""==i.value)if(null!=n.value&&""!=n.value){var u=Array.from(n.options).find(e=>1==e.selected);1==a&&(n.textContent="",null!=o&&""==o.value&&(n.appendChild(o),n.appendChild(u),n.scrollTo(0,1)),""!=u.value&&(n.appendChild(u),n.scrollTo(0,0))),e.Results.filter(e=>e.id!=u.value).forEach(e=>{var t=document.createElement("option");t.text=e.text,t.value=e.id,n.appendChild(t)})}else 1==a&&(n.textContent="",null!=o&&n.appendChild(o)),e.Results.forEach(e=>{var t=document.createElement("option");t.text=e.text,t.value=e.id,n.appendChild(t)});else e.Results.forEach(e=>{var t=document.createElement("option");t.text=e.text,t.value=e.id,n.appendChild(t)});a>1&&(n.scrollTop=s),n.options.length>=l&&n.setAttribute("estaCompleto","true")}).catch((function(e){console.log("Se encontró un error",e)}))}n.addEventListener("change",t=>{i.style.display="none",n.style.position="static",n.style.zIndex="1",n.removeAttribute("size"),n.setAttribute("pagina","1"),n.style.width="100%";var l=Array.from(n.options).find(e=>1==e.selected);n.setAttribute("textoSeleccionado",l.text),n.setAttribute("valorSeleccionado",l.value),null!=e.onchange&&"function"==typeof e.onchange&&e.onchange()}),n.addEventListener("scroll",e=>{if("block"==i.style.display&&"false"==n.getAttribute("estaCompleto")&&e.target.offsetHeight+e.target.scrollTop>=e.target.scrollHeight){null!=i.value&&""!=i.value||(i.placeholder=r),i.disabled=!0,n.disabled=!0;var t=parseInt(n.getAttribute("pagina"));n.setAttribute("pagina",t+1),d(l)}}),n.addEventListener("mousedown",e=>{"select"==e.target.localName&&e.preventDefault()}),n.addEventListener("click",e=>{t="",n.setAttribute("estaCompleto","false"),"select"==e.target.localName&&(i.value="",d(l))}),i.addEventListener("keyup",(function(e){n.setAttribute("estaCompleto","false"),null!=e.target.value&&""!=e.target.value&&(t=e.target.value,n.setAttribute("pagina","1")),null!=e.target.value&&""!=e.target.value||(t=""),n.textContent="",d(l)})),window.addEventListener("click",(function(e){if(!a.contains(e.target)){var t=Array.from(n.options).find(e=>1==e.selected&&""!=e.value);i.disabled=!1,n.disabled=!1,i.placeholder="",i.style.display="none",n.style.position="relative",n.style.zIndex="1",n.removeAttribute("size"),n.setAttribute("pagina","1"),n.setAttribute("estaCompleto","false"),n.style.width="100%",null!=n.value&&""!=n.value&&void 0!==t||s()}})),window.addEventListener("resize",(function(e){n.style.width=i===document.activeElement?i.getBoundingClientRect().width+"px":"100%"}))}