
/* Initialization */
$( document ).ready(function() {
    initSubHeader();
    initSidebarNav();
    initScrollMenu();
});

function initSubHeader(){

    //Fixed subheader
    $(window).resize(function(){
        $('.sub-header').css('width', $('.container').css('width'));
    });

    $(window).scroll(function () {
        var y = $(this).scrollTop();

        if (y>=$('.header').height()) {
            $('.sub-header').css({
                'position': 'fixed',
                'z-index': '999',
                'top': '0',
                'width': $('.container').css('width')
            });
            $('.tree-icon').css('display', 'none');

        }
        else {
            $('.sub-header').css('position', 'static');
            $('.tree-icon').css('display', 'inline-block');

        }

    });


    //Avoid Bootstrap dropdown auto-close when clicking
    $('.toolbar-dropdown ul.dropdown-menu').on('click', function(event) {
        event.stopPropagation();
    });

    //Tree Toggle button
    $('.tree-icon').click(function(){
        $(this).toggleClass('tree-closed');
        if($(window).width() < 992)
            toggleSideNavMobile()
        else
            toggleSideNavDesktop();
    });

    function toggleSideNavDesktop(){
        if($('.article-content').hasClass('col-md-7')){
            $('.article-content').removeClass('col-md-7').addClass('col-md-10');
            $('.sidebar-nav').toggleClass('hidecontent-desktop');
        }
        else{
            $('.article-content').removeClass('col-md-10').addClass('col-md-7');
            $('.sidebar-nav').toggleClass('hidecontent-desktop');
        }
    }
    function toggleSideNavMobile(){
        $('.sidebar-nav').toggleClass('showcontent');
        $('.version-selector').toggleClass('hidecontent');
        $('.article-content').toggleClass('hidecontent');
        $('.toolbar').toggleClass('hidecontent');
        $('.search-box').toggleClass('fullwidth');
    }

    //Remove Search input box placeholder on focus
    $('.search-field').focus(function(){
        $(this).data('placeholder',$(this).attr('placeholder'))
        $(this).attr('placeholder','');
    });
    $('.search-field').blur(function(){
        $(this).attr('placeholder',$(this).data('placeholder'));
    });
}



function initScrollMenu(){
    //Scroll-menu active links

    $(window).scroll(function () {

        var y = $(this).scrollTop();

        $('.scroll-menu-link').each(function (event) {
            if (y >= $($(this).attr('href')).offset().top - 20) {
                //if ($($(this).attr('href')).offset().top - y <= $(window).height()/2) {
                $('.scroll-menu-link').not(this).removeClass('active');
                $(this).addClass('active');
            }
        });

    });
    //Scroll-menu smooth scroll
    $('a[href*=#]:not([href=#]):not([role=tab])').click(function () {
        if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
            if (target.length) {
                $('html,body').animate({
                    scrollTop: (target.offset().top - 20)
                }, 850);
                return false;
            }
        }
    });
}

/* Setting sidebar tree nav */
function initSidebarNav(){

    //Collapse all lists
    $('.sidebar-nav nav li:has(ul)').addClass('parent_li');
    openExpandedSubtree();


    //Active item
    var activeItem = $('.sidebar-nav nav li.active');

    if(activeItem.length > 0){
        place_scroll_marker(activeItem, 'active-marker');
    }




    $('.sidebar-nav nav li.parent_li > i').on('click', function (e) {

        var parent = $(this).parent('li.parent_li'),
            children = parent.find('> ul');

        // Set active category
        if(parent.parent().hasClass('tree')) {
            collapseLists('fast');

        }

        place_scroll_marker(parent, 'marker');

        // Show/hide a sublist
        if (children.is(":visible")) {
            children.hide('fast');
            $(this).addClass('glyphicon-chevron-right').removeClass('glyphicon-chevron-down');


            /* Remove active trail from the node to the childrens */
            parent.removeClass('expanded');
            parent.find('li.expanded').removeClass('expanded');

            //Hide active-marker
            if(children.find('.active'))
                $('.active-marker').hide('fast');


        } else {
            children.show('fast');
            $(this).addClass('glyphicon-chevron-down').removeClass('glyphicon-chevron-right');
            parent.addClass('expanded');

            if(children.find('.active').is(':visible')){
                $('.active-marker').show('fast');
            }
        }

        e.stopPropagation();
    });

    $('.sidebar-nav nav li').hover(function() {
        $('.marker').show();
        place_scroll_marker($(this), "marker");
    },function() {
        if(!$('.tree').is(':hover'))
            $('.marker').hide();
    });

    function collapseLists(speed) {
        $('.sidebar-nav nav li.parent_li').removeClass('expanded');
        $('.sidebar-nav nav li.parent_li > ul').hide(speed);
        $('.sidebar-nav nav li.parent_li > i').addClass('glyphicon-chevron-right').removeClass('glyphicon-chevron-down');
    }


    function openExpandedSubtree(){
        $('.sidebar-nav nav li.parent_li > ul').hide();
        $('.sidebar-nav nav li.parent_li > i').addClass('glyphicon-chevron-right').removeClass('glyphicon-chevron-down');
        $('.sidebar-nav nav li.parent_li.expanded > ul').show();
        $('.sidebar-nav nav li.parent_li.expanded > i').addClass('glyphicon-chevron-down').removeClass('glyphicon-chevron-right');
    }

    function place_scroll_marker(elem, markerClass) {
        var offsetTop = elem.offset().top,
            offsetLeft = $(".tree").left,
            link = elem.find('> a'),
            linkHeight = link.height() + parseInt(elem.css('padding-top')) + parseInt(elem.css('padding-bottom')) + parseInt(link.css('padding-bottom')) + parseInt(link.css('padding-bottom'));
            //linkHeight = link.height() + parseInt(link.css('padding-bottom')) + parseInt(link.css('padding-bottom'));
        $(".sidebar-nav ." + markerClass).show();
        $(".sidebar-nav ." + markerClass).offset({top: offsetTop, left: offsetLeft});
        $(".sidebar-nav ." + markerClass).height(linkHeight);
    }
}

